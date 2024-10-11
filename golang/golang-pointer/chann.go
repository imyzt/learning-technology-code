package main

import "fmt"

func chann() {

	//通过<-，写入和读取管道
	ch := make(chan int)
	ch1 := make(chan<- int)

	//只允许发送的管道
	_ = make(chan<- int)
	// 只允许接收的管道
	_ = make(<-chan int)

	// 创建一个协程用于并发
	go func() {
		for i := 0; i < 10; i++ {
			ch <- i
		}
		close(ch)
	}()

	for v := range ch {
		println(v)
	}

	val := <-ch
	fmt.Println(val)

	select {
	case val1 := <-ch:
		fmt.Printf("val1=%d\n", val1)
	case ch1 <- 1:
		fmt.Println("写入chan1")
	}
}
