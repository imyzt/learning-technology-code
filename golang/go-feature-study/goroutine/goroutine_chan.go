package main

import (
	"fmt"
	"sync"
	"time"
)

// 通过chan来控制goroutine的并发速率

func worker(id int, sem chan struct{}, wg *sync.WaitGroup) {
	defer wg.Done()
	fmt.Println("worker starting, id=", id)
	time.Sleep(1 * time.Second)
	fmt.Println("worker done, id=", id)
	// 消费chan
	<-sem
}

func main2() {
	var wg sync.WaitGroup
	sem := make(chan struct{}, 5)
	for i := 0; i < 20; i++ {
		wg.Add(1)
		sem <- struct{}{}
		go worker(i, sem, &wg)
	}

	// 等待所有协程完成
	wg.Wait()
	fmt.Println("all workers done")
}
