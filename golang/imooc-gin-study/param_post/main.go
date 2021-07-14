package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

/**
获取post请求的参数
*/
func main() {

	r := gin.Default()

	r.POST("/test", func(context *gin.Context) {
		firstName := context.PostForm("firstName")
		// 默认值
		lastName := context.DefaultPostForm("lastName", "defaultLastName")
		context.JSON(http.StatusOK, gin.H{
			"firstName": firstName,
			"lastName":  lastName,
		})
	})

	r.Run()
}
