package main

import (
	"fmt"
	"github.com/gorilla/websocket"
	"golangWebSocket/impl"
	"net/http"
	"time"
)

var upgrader = websocket.Upgrader{
	// 允许跨域
	CheckOrigin: func(r *http.Request) bool {
		return true
	},
}

func wsHandler(w http.ResponseWriter, r *http.Request) {

	var (
		wsConn *websocket.Conn
		err    error
		data   []byte
		conn   *impl.Connection
	)

	if wsConn, err = upgrader.Upgrade(w, r, nil); err != nil {
		fmt.Println("Upgrade fail!")
		return
	}

	if conn, err = impl.InitConnection(wsConn); err != nil {
		goto ERR
	}

	go func() {
		for {
			if err = conn.WriteMessage([]byte("heartbeat")); err != nil {
				return
			}
			time.Sleep(1 * time.Second)
		}
	}()

	for {
		if data, err = conn.ReadMessage(); err != nil {
			goto ERR
		}
		if err := conn.WriteMessage(data); err != nil {
			goto ERR
		}
	}

ERR:
	conn.Close()
}

func main() {

	http.HandleFunc("/ws", wsHandler)

	http.ListenAndServe("0.0.0.0:7777", nil)

}
