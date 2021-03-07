package main

import (
	"fmt"
	"net/http"
)

func main() {

	http.HandleFunc("/index", func(writer http.ResponseWriter, request *http.Request) {
		name := request.FormValue("name")
		fmt.Println(name)
		writer.Write([]byte(name + "_helloworld"))
	})

	http.ListenAndServe("0.0.0.0:9080", nil)
}
