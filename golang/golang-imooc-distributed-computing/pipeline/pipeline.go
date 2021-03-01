package pipeline

import (
	"sort"
)

func ArraySource(source ...int) <-chan int {
	out := make(chan int)
	go func() {
		for _, num := range source {
			out <- num
		}
		close(out)
	}()
	return out
}

func InMemSort(source <-chan int) <-chan int {
	out := make(chan int)
	go func() {
		// Read into Memory
		var arr []int
		for v := range source {
			arr = append(arr, v)
		}

		// Sort
		sort.Ints(arr)

		// Output
		for _, v := range arr {
			out <- v
		}
		close(out)
	}()
	return out
}

func Merge(in1, in2 <-chan int) <-chan int {
	out := make(chan int)
	go func() {
		v1, ok1 := <-in1
		v2, ok2 := <-in2
		for ok1 || ok2 {
			// 如果in2没有数据 或者 in1有数据并且v1<=v2时,将v1数据加入
			if !ok2 || (ok1 && v1 <= v2) {
				out <- v1
				v1, ok1 = <-in1
			} else {
				// in2有数据并且v1大于v2
				out <- v2
				v2, ok2 = <-in2
			}
		}
		close(out)
	}()
	return out
}
