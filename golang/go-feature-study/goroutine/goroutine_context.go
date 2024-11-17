package main

import (
	"context"
	"fmt"
	"sync"
	"time"
)

// 通过context包来管理协程的生命周期
func worker2(id int, ctx context.Context, wg *sync.WaitGroup) {
	defer wg.Done()
	fmt.Println("worker starting, id=", id)

	for {
		select {
		case <-ctx.Done():
			fmt.Println("worker done, id=", id)
			return
		default:
			time.Sleep(1 * time.Second)
		}
	}
}

func main3() {
	var wg sync.WaitGroup
	ctx, cancel := context.WithCancel(context.Background())

	for i := 0; i < 20; i++ {
		wg.Add(1)
		go worker2(i, ctx, &wg)
	}

	time.Sleep(5 * time.Second)
	cancel()
	wg.Wait()
	fmt.Println("all workers done")
}
