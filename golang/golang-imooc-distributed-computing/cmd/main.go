package main

import (
	"fmt"
	"golang-imooc-distributed-computing/pipeline"
)

func main() {

	retChan := pipeline.Merge(pipeline.InMemSort(
		pipeline.ArraySource(4, 885, 3, 9, 2, 0, 8),
	), pipeline.InMemSort(
		pipeline.ArraySource(34, 52, 33, 11, 32, 14, 98),
	))

	for num := range retChan {
		fmt.Println(num)
	}

}
