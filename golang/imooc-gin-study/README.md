# gin quick-start

## httpMethod start

```go
package main

import "github.com/gin-gonic/gin"

func main() {

	// 简单gin使用
	r := gin.Default()
	r.GET("/get", func(context *gin.Context) {
		context.JSON(200, gin.H{
			"message": "get",
		})
	})
	r.POST("/post", func(context *gin.Context) {
		context.JSON(200, gin.H{
			"message": "post",
		})
	})
	r.DELETE("/delete", func(context *gin.Context) {
		context.JSON(200, gin.H{
			"message": "delete",
		})
	})
	r.PUT("/put", func(context *gin.Context) {
		context.JSON(200, gin.H{
			"message": "put",
		})
	})
	// 支持任意请求
	r.Any("/any", func(context *gin.Context) {
		context.String(200, "any")
	})

	r.Run()
}

```

## rest参数
```go
r.GET(":name/:id", func(context *gin.Context) {
    context.JSON(200, gin.H{
        "name": context.Param("name"),
        "id": context.Param("id"),
    })
})
```

## 开放静态资源
```go
// 静态文件夹
r.Static("/asserts", "./asserts")
// 静态文件目录
r.StaticFS("/static", http.Dir("/Users/imyzt/Downloads/"))
// 静态文件
r.StaticFile("/test.html", "./test.html")
```

## 泛匹配
```go
r.GET("/name/*action", func(context *gin.Context) {
    context.String(200, "helloWorld")
})
```

## 获取GET请求参数

```go
r.GET("/test", func(context *gin.Context) {
    firstName := context.Query("firstName")
    // 默认值
    lastName := context.DefaultQuery("lastName", "defaultLastName")
    context.JSON(http.StatusOK, gin.H{
        "firstName": firstName,
        "lastName":  lastName,
    })
})
```

## 获取POST form-data请求参数
```go
r.POST("/test", func(context *gin.Context) {
    firstName := context.PostForm("firstName")
    // 默认值
    lastName := context.DefaultPostForm("lastName", "defaultLastName")
    context.JSON(http.StatusOK, gin.H{
        "firstName": firstName,
        "lastName":  lastName,
    })
})
```

## 获取POST body请求参数
```go
r.POST("/test", func(context *gin.Context) {

    bodyBytes, err := ioutil.ReadAll(context.Request.Body)
    if err != nil {
        context.String(http.StatusBadRequest, err.Error())
        context.Abort()
    }

    // 回传, 方便获取参数
    context.Request.Body = ioutil.NopCloser(bytes.NewBuffer(bodyBytes))

    // 默认值
    name := context.DefaultPostForm("name", "defaultName")
    context.String(http.StatusOK, "%s - %s", name, string(bodyBytes))
})
```
CURL输出
```
//curl -X POST "localhost:8080/test" -d 'name=xxx&bbb=ddd'
//xxx - name=xxx&bbb=ddd%
```

## GET/POST请求参数映射到struct
```go
type Student struct {
	Name string `form:"name" json:"name"`
	Addr string `form:"addr" json:"addr"`
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

```

# 参数验证

## 普通验证
```go
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
```

## 自定义验证
```go
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
```

## i18n验证
```go
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
	Name string `form:"name" json:"name" validate:"required"`
	Age      int       `form:"age" json:"age" validate:"required,gt=10"`
	Birthday time.Time `form:"birthday" time_format:"2006-01-02 15:04:05" json:"birthday"`
}

var (
	Uni *ut.UniversalTranslator
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

```

# 中间件

## log/recovery

```go
/**
使用中间件
gin.Default()默认包含了两个中间件
engine.Use(Logger(), Recovery())
 */
func main() {

	r := gin.New()
	logFile, _ := os.Create("./gin.log")
	gin.DefaultWriter = io.MultiWriter(logFile)
	gin.DefaultErrorWriter = io.MultiWriter(logFile)

	// 使用日志中间件, 将日志打印到文件
	r.Use(gin.Logger())
	// 使用recovery, 防止panic之后系统崩溃
	r.Use(gin.Recovery())

	r.GET("test", func(context *gin.Context) {
		context.String(http.StatusOK, "OK")

		// 如果没有gin.Recovery(), 访问后系统崩溃
		// panic("test")
	})
	r.Run()
}
```

## 自定义中间件

```go
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
```


# 优雅停机

```go
func main() {

	r := gin.Default()

	r.GET("test", func(context *gin.Context) {
		time.Sleep(time.Second*10)
		context.String(http.StatusOK, "OK!")
	})

	// r.Run()会阻塞, server.ListenAndServe()不会阻塞
	server := &http.Server{
		Addr:    ":8081",
		Handler: r,
	}

	// 启动协程捕获异常
	go func() {
		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			// 非 关闭服务异常时
			log.Fatalf("listen: %s\n", err)
		}
	}()

	quit := make(chan os.Signal)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	// 阻塞等待系统通知, 通道收到消息时证明系统发送停机消息过来
	<- quit
	log.Println("shutdown server...")

	// 上下文等待10秒, 不接受新请求让之前的请求处理完毕
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	if err := server.Shutdown(ctx); err != nil {
		log.Fatal("server shutdown error", err)
	}

	log.Println("server exiting...")

}
```