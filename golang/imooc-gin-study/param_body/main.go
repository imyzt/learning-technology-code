package main

import (
	"bytes"
	"github.com/gin-gonic/gin"
	"io/ioutil"
	"net/http"
)

func main() {

	r := gin.Default()

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

	r.Run()

	//curl -X POST "localhost:8080/test" -d 'name=xxx&bbb=ddd'
	//xxx - name=xxx&bbb=ddd%

}
