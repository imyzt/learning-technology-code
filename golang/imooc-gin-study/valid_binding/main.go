package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"time"
)

/**
更多验证规则
https://pkg.go.dev/gopkg.in/bluesuncorp/validator.v8#section-documentation
*/
type Student struct {
	Name string `form:"name" json:"name" binding:"required"`
	// binding 逗号分割表示并且, | 分割表示或者(满足一项)
	Age      int       `form:"age" json:"age" binding:"required,gt=10"`
	Birthday time.Time `form:"birthday" time_format:"2006-01-02 15:04:05" json:"birthday"`
}

func main() {

	r := gin.Default()
	r.POST("test", func(context *gin.Context) {

		var student Student
		// 根据不同的context-type,做不同的映射
		if err := context.ShouldBind(&student); err != nil {
			context.String(http.StatusBadRequest, "student bind error %v", err.Error())
			return
		}

		context.JSON(http.StatusOK, student)
	})

	r.Run()

}
