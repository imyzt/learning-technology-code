package main

import "net/http"

func httpHandler(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("hello"))
}

func main() {

	http.HandleFunc("/http", httpHandler)

	http.ListenAndServe("0.0.0.0:7777", nil)

}
