package main

import (
	"context"
	"github.com/gin-gonic/gin"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"
)

func main() {

	r := gin.Default()

	r.GET("test", func(context *gin.Context) {
		time.Sleep(time.Second * 10)
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
			// 非关闭服务异常时
			log.Fatalf("listen: %s\n", err)
		}
	}()

	quit := make(chan os.Signal)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	// 阻塞等待系统通知, 通道收到消息时证明系统发送停机消息过来
	<-quit
	log.Println("shutdown server...")

	// 上下文等待10秒, 不接受新请求让之前的请求处理完毕
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	if err := server.Shutdown(ctx); err != nil {
		log.Fatal("server shutdown error", err)
	}

	log.Println("server exiting...")

}
