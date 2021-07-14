package main

import "github.com/gin-gonic/gin"

/**
/name/下任何请求都匹配到下面
*/
func main() {

	r := gin.Default()

	r.GET("/name/*action", func(context *gin.Context) {
		context.String(200, "helloWorld")
	})

	err := r.Run()
	if err != nil {
		panic(err)
	}
}
