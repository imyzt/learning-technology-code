package main

import (
	"fmt"
	"time"
)

/*
消费端优雅关闭流
*/
func graceChannelClose() {

	channel := make(chan time.Time, 100)
	closeChan := make(chan bool, 1)

	go func() {
		for {
			select {
			case <-closeChan:
				fmt.Println("sender quit")
				return
			default:
				channel <- time.Now()
				time.Sleep(time.Millisecond * 100)
			}
		}
	}()

	go func() {
		for i := 0; i < 100; i++ {
			now := <-channel
			fmt.Println(i, now.Format("2006-01-02 15:04:05"))
			if i == 50 {
				closeChan <- true
				close(channel)
				return
			}
		}
	}()

	<-make(chan bool)
}

/*
普通通道关闭直接生产端异常

... 时间打印
2022-04-12 21:18:18
2022-04-12 21:18:18
panic: send on closed channel

goroutine 4 [running]:
main.normalChannelCloseErr.func1(0xc00007c120)
        /.../learning-technology-code/golang/go-feature-study/chanClose.go:23 +0x5a
created by main.normalChannelCloseErr
        /.../learning-technology-code/golang/go-feature-study/chanClose.go:21 +0x58
*/
func normalChannelCloseErr() {
	channel := make(chan time.Time, 100)

	go func() {
		for {
			channel <- time.Now()
			time.Sleep(time.Millisecond * 100)
		}
	}()

	go func() {
		for i := 0; i < 100; i++ {
			if i == 50 {
				close(channel)
				return
			}
			now := <-channel
			fmt.Println(now.Format("2006-01-02 15:04:05"))
		}
	}()
}
