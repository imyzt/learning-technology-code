package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

var (
	ipList = []string{"127.0.0.1"}
)

/**
自定义中间件, 只需要返回`gin.HandlerFunc`即可
*/
func IpAuthMiddleware() gin.HandlerFunc {
	return func(context *gin.Context) {
		ip := context.ClientIP()
		flag := false
		for _, whitelistIp := range ipList {
			if ip == whitelistIp {
				flag = true
			}
		}

		if !flag {
			context.String(http.StatusForbidden, "not in IP whitelist")
			context.Abort()
			return
		}
	}
}

func main() {
	r := gin.Default()
	// 使用自定义中间件
	r.Use(IpAuthMiddleware())
	r.GET("test", func(context *gin.Context) {
		context.String(http.StatusOK, "OK")
	})
	r.Run()
}
