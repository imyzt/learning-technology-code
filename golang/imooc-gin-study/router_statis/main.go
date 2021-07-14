package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

/**
需要进入到当前包目录,执行go build -o router_statis && ./router_statis
goland会找不到文件
*/
func main() {

	r := gin.Default()

	// 静态文件夹
	r.Static("/asserts", "./asserts")
	// 静态文件目录
	r.StaticFS("/static", http.Dir("/Users/imyzt/Downloads/"))
	// 静态文件
	r.StaticFile("/test.html", "./test.html")

	r.Run("0.0.0.0:8081")

}
