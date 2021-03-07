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