package main

import (
	"log"
	"net/http"
	"os"
)

func main() {
	args := os.Args
	port := ":8080"
	if len(args) == 1 {
		panic("ussage:SimpleFileServer_GO <Dir> <Port:8080>")
	} else if len(args) == 3 {
		port = ":" + args[2]
	}
	path := args[1]

	log.Printf("open fileServer [%v] in [http://localhost%v]", path, port)

	http.Handle("/", http.FileServer(http.Dir(path)))
	log.Fatal(http.ListenAndServe(port, nil))
}
