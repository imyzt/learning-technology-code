package main

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"github.com/gin-gonic/gin/binding"
	"github.com/go-playground/validator/v10"
	"net/http"
	"time"
)

/**
更多验证规则
https://pkg.go.dev/gopkg.in/bluesuncorp/validator.v8#section-documentation
*/
type Booking struct {
	// 时间必须符合bookabledate检查
	ChinkIn time.Time `form:"checkIn" json:"chink_in" binding:"required,bookabledate" time_format:"2006-01-02"`
	// 时间必须大于ChinkIn
	ChinkOut time.Time `form:"checkOut" json:"chink_out" binding:"required,gtfield=ChinkIn" time_format:"2006-01-02"`
}

func bookableDate(fl validator.FieldLevel) bool {

	if date, ok := fl.Field().Interface().(time.Time); ok {
		// 大于今天
		return date.Unix() > time.Now().Unix()
	}
	return false
}

func main() {

	r := gin.Default()

	// 注册验证规则
	if v, ok := binding.Validator.Engine().(*validator.Validate); ok {
		err := v.RegisterValidation("bookabledate", bookableDate)
		if err != nil {
			for _, e := range err.(validator.ValidationErrors) {
				fmt.Println(e)
			}
		}
	}

	r.POST("test", func(context *gin.Context) {

		var booking Booking
		// 根据不同的context-type,做不同的映射
		if err := context.ShouldBind(&booking); err != nil {
			context.String(http.StatusBadRequest, "booking bind error %v", err.Error())
			return
		}

		context.JSON(http.StatusOK, booking)
	})

	r.Run()
}
