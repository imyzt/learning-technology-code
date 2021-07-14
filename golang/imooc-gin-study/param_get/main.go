package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

/**
获取get请求的参数
*/
func main() {

	r := gin.Default()

	r.GET("/test", func(context *gin.Context) {
		firstName := context.Query("firstName")
		// 默认值
		lastName := context.DefaultQuery("lastName", "defaultLastName")
		context.JSON(http.StatusOK, gin.H{
			"firstName": firstName,
			"lastName":  lastName,
		})
	})

	r.Run()

}
