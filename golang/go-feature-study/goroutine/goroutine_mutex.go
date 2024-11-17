package main

import (
	"fmt"
	"sync"
	"time"
)

// 使用mutex信号量来控制goroutine的流速

var activeWorkers int

//var limitWorker int

func worker3(id, limit int, sem *sync.Mutex, wg *sync.WaitGroup) {
	defer wg.Done()
	fmt.Println("worker starting, id=", id)
	time.Sleep(1 * time.Second)
	fmt.Println("worker done, id=", id)

	sem.Lock()
	if activeWorkers < limit {
		activeWorkers++
	} else {
		sem.Unlock()
		// 如果超过并发限制， 等1秒后重试
		time.Sleep(1 * time.Second)
		worker3(id, limit, sem, wg)
		return
	}
	sem.Unlock()
}

func main() {
	//limitWorker = 5
	var wg sync.WaitGroup
	sem := &sync.Mutex{}
	for i := 0; i < 20; i++ {
		wg.Add(1)
		go worker3(i, 5, sem, &wg)
	}
	wg.Wait()
	fmt.Println("all workers done")
}
