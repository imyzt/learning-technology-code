package main

import "github.com/gin-gonic/gin"

/**
参数作为请求URL - rest
*/
func main() {

	r := gin.Default()

	r.GET(":name/:id", func(context *gin.Context) {
		context.JSON(200, gin.H{
			"name": context.Param("name"),
			"id":   context.Param("id"),
		})
	})

	r.Run("0.0.0.0:8082")
}
