package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"time"
)

type Student struct {
	Name     string    `form:"name" json:"name"`
	Addr     string    `form:"addr" json:"addr"`
	Birthday time.Time `form:"birthday" time_format:"2006-01-02 15:04:05" json:"birthday"`
}

func main() {

	r := gin.Default()

	r.POST("test", test)
	r.GET("test", test)

	r.Run()
}

func test(context *gin.Context) {

	var student Student
	// 根据不同的context-type,做不同的映射
	if err := context.ShouldBind(&student); err != nil {
		context.String(http.StatusBadRequest, "student bind error %v", err.Error())
		return
	}

	context.JSON(http.StatusOK, student)
}
