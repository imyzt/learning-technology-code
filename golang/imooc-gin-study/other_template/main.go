package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

func main() {

	r := gin.Default()
	r.LoadHTMLGlob("template/*")

	r.GET("index", func(context *gin.Context) {

		context.HTML(http.StatusOK, "index.html", gin.H{
			"title": context.Query("title"),
		})
	})
	r.Run()
}
