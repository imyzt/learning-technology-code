package impl

import (
	"errors"
	"fmt"
	"github.com/gorilla/websocket"
	"sync"
)

type Connection struct {
	wsConn    *websocket.Conn
	inChan    chan []byte
	outChan   chan []byte
	closeChan chan byte
	isClosed  bool
	mutex     sync.Mutex
}

// API函数
func InitConnection(wsConn *websocket.Conn) (conn *Connection, err error) {
	conn = &Connection{
		wsConn:    wsConn,
		inChan:    make(chan []byte, 500),
		outChan:   make(chan []byte, 500),
		closeChan: make(chan byte, 1),
		isClosed:  false,
	}

	// 启动读协程
	go conn.readLoop()

	// 启动写协程
	go conn.writeLoop()

	return
}

// 方法
func (conn *Connection) ReadMessage() (data []byte, err error) {
	select {
	case data = <-conn.inChan:
	case <-conn.closeChan:
		err = errors.New("connection is closed")
	}
	return
}
func (conn *Connection) WriteMessage(data []byte) (err error) {
	select {
	case conn.outChan <- data:
	case <-conn.closeChan:
		err = errors.New("connection is closed")
	}
	return
}
func (conn *Connection) Close() {
	// 线程安全, 可重入的close
	conn.wsConn.Close()

	conn.mutex.Lock()
	if !conn.isClosed {
		// 这行代码只能执行一次, 需要加锁
		close(conn.closeChan)
		conn.isClosed = true
	}
	conn.mutex.Unlock()
}

func (conn *Connection) readLoop() {
	var (
		data []byte
		err  error
	)
	for {
		if _, data, err = conn.wsConn.ReadMessage(); err != nil {
			goto ERR
		}

		// 阻塞在这里, 等待inChan有空闲的位置
		select {
		case conn.inChan <- data:
		case <-conn.closeChan:
			// closeChan关闭的时候
			fmt.Println("readLoop close")
			goto ERR
		}
	}
ERR:
	conn.Close()
}

func (conn *Connection) writeLoop() {
	var (
		data []byte
		err  error
	)

	for {

		select {
		case data = <-conn.outChan:
		case <-conn.closeChan:
			fmt.Println("writeLoop close")
			goto ERR
		}

		if err = conn.wsConn.WriteMessage(websocket.TextMessage, data); err != nil {
			goto ERR
		}
	}
	return
ERR:
	conn.Close()
}
