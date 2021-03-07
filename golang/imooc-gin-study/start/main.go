package main

import "github.com/gin-gonic/gin"

func main() {

	// 简单gin使用
	r := gin.Default()
	r.GET("ping", func(context *gin.Context) {
		context.JSON(200, gin.H{
			"message": "pong",
		})
	})
	r.Run()
}
