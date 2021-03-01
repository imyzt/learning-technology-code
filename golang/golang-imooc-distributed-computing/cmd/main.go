package main

import (
	"bufio"
	"fmt"
	"golang-imooc-distributed-computing/pipeline"
	"os"
)

func main() {

	const filename = "small.in"
	const n = 50000000

	file, err := os.Create(filename)
	if err != nil {
		panic(err)
	}
	defer file.Close()

	randomSource := pipeline.RandomSource(n)
	writer := bufio.NewWriter(file)
	pipeline.WriteSlink(writer, randomSource)
	writer.Flush()

	openFile, err := os.Open(filename)
	if err != nil {
		panic(err)
	}
	defer file.Close()

	readerSource := pipeline.ReaderSource(bufio.NewReader(openFile))
	count := 0
	for v := range readerSource {
		fmt.Println(v)
		if count > 100 {
			break
		}
		count++
	}

}

func mergeDemo() {

	retChan := pipeline.Merge(pipeline.InMemSort(
		pipeline.ArraySource(4, 885, 3, 9, 2, 0, 8),
	), pipeline.InMemSort(
		pipeline.ArraySource(34, 52, 33, 11, 32, 14, 98),
	))

	for num := range retChan {
		fmt.Println(num)
	}
}
