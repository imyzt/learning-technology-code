package main

import (
	"fmt"
	"github.com/gorilla/websocket"
	"net/http"
)

var upgrader = websocket.Upgrader{
	// 允许跨域
	CheckOrigin: func(r *http.Request) bool {
		return true
	},
}

func wsHandler(w http.ResponseWriter, r *http.Request) {

	var (
		conn *websocket.Conn
		err  error
		data []byte
	)

	if conn, err = upgrader.Upgrade(w, r, nil); err != nil {
		fmt.Println("Upgrade fail!")
		return
	}

	for {

		if _, data, err = conn.ReadMessage(); err != nil {
			goto ERR
		}

		if err = conn.WriteMessage(websocket.TextMessage, data); err != nil {
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
