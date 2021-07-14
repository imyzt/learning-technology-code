package main

import (
	"github.com/gin-gonic/gin"
	"io"
	"net/http"
	"os"
)

/**
使用中间件
gin.Default()默认包含了两个中间件
engine.Use(Logger(), Recovery())
*/
func main() {

	r := gin.New()
	logFile, _ := os.Create("./gin.log")
	gin.DefaultWriter = io.MultiWriter(logFile)
	gin.DefaultErrorWriter = io.MultiWriter(logFile)

	// 使用日志中间件, 将日志打印到文件
	r.Use(gin.Logger())
	// 使用recovery, 防止panic之后系统崩溃
	r.Use(gin.Recovery())

	r.GET("test", func(context *gin.Context) {
		context.String(http.StatusOK, "OK")

		// 如果没有gin.Recovery(), 访问后系统崩溃
		// panic("test")
	})
	r.Run()
}
