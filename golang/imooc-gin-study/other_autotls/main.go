package main

import (
	"github.com/gin-gonic/autotls"
	"github.com/gin-gonic/gin"
	"log"
	"net/http"
)

func main() {

	r := gin.Default()
	gin.SetMode(gin.ReleaseMode)
	r.GET("ping", func(context *gin.Context) {
		context.String(http.StatusOK, "pong")
	})
	log.Fatal(autotls.Run(r, "go.imyzt.top"))
}
