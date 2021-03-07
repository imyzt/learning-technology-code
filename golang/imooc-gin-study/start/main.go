package main

import "github.com/gin-gonic/gin"

func main() {

	// 简单gin使用
	r := gin.Default()
	r.GET("/get", func(context *gin.Context) {
		context.JSON(200, gin.H{
			"message": "get",
		})
	})
	r.POST("/post", func(context *gin.Context) {
		context.JSON(200, gin.H{
			"message": "post",
		})
	})
	r.DELETE("/delete", func(context *gin.Context) {
		context.JSON(200, gin.H{
			"message": "delete",
		})
	})
	r.PUT("/put", func(context *gin.Context) {
		context.JSON(200, gin.H{
			"message": "put",
		})
	})
	// 支持任意请求
	r.Any("/any", func(context *gin.Context) {
		context.String(200, "any")
	})

	r.Run()
}
