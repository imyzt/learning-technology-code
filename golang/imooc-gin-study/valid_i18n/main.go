package main

import (
	"github.com/gin-gonic/gin"
	en2 "github.com/go-playground/locales/en"
	zh2 "github.com/go-playground/locales/zh"
	ut "github.com/go-playground/universal-translator"
	"gopkg.in/go-playground/validator.v9"
	en_translations "gopkg.in/go-playground/validator.v9/translations/en"
	zh_translations "gopkg.in/go-playground/validator.v9/translations/zh"
	"net/http"
	"time"
)

/**
更多验证规则
https://pkg.go.dev/gopkg.in/bluesuncorp/validator.v8#section-documentation
*/
type Student struct {
	Name     string    `form:"name" json:"name" validate:"required"`
	Age      int       `form:"age" json:"age" validate:"required,gt=10"`
	Birthday time.Time `form:"birthday" time_format:"2006-01-02 15:04:05" json:"birthday"`
}

var (
	Uni      *ut.UniversalTranslator
	Validate *validator.Validate
)

func main() {

	// 初始化验证器
	initValidator()

	r := gin.Default()

	r.GET("test", func(context *gin.Context) {

		// 根据不同语言,加载不同的验证器
		locale := context.DefaultQuery("locale", "zh")
		translator, _ := Uni.GetTranslator(locale)
		switch locale {
		case "zh":
			_ = zh_translations.RegisterDefaultTranslations(Validate, translator)
		case "en":
			_ = en_translations.RegisterDefaultTranslations(Validate, translator)
		default:
			_ = zh_translations.RegisterDefaultTranslations(Validate, translator)
		}

		// 根据不同的context-type,做不同的映射
		var student Student
		if err := context.ShouldBind(&student); err != nil {
			context.String(http.StatusBadRequest, "student bind error %v", err.Error())
			context.Abort()
			return
		}

		// 验证
		if valid(context, student, translator) {
			return
		}

		context.JSON(http.StatusOK, student)
	})

	r.Run()

	//➜  github.com curl -X GET "localhost:8080/test?name=xxx&bbb=ddd&locale=zh&age=2"
	//student validate error [Age必须大于10]%                                                                                                                                                                     ➜  github.com curl -X GET "localhost:8080/test?name=xxx&bbb=ddd&locale=en&age=2"
	//➜  github.com curl -X GET "localhost:8080/test?name=xxx&bbb=ddd&locale=en&age=2"
	//student validate error [Age must be greater than 10]%

}

func valid(context *gin.Context, student Student, translator ut.Translator) bool {
	if err := Validate.Struct(student); err != nil {
		errors := err.(validator.ValidationErrors)
		sliceErrors := []string{}
		for _, e := range errors {
			sliceErrors = append(sliceErrors, e.Translate(translator))
		}
		context.String(http.StatusBadRequest, "student validate error %v", sliceErrors)
		context.Abort()
		return true
	}
	return false
}

func initValidator() {
	Validate = validator.New()
	zh := zh2.New()
	en := en2.New()
	Uni = ut.New(zh, en)
}
