package main

import (
	"bufio"
	"fmt"
	"golang-imooc-distributed-computing/pipeline"
	"os"
)

func main() {

	p := createPipeline("large.in", 800000000, 4)
	writeToFile(p, "large.out")
	printFile("large.out")

}

func printFile(filename string) {
	file, err := os.Create(filename)
	if err != nil {
		panic(err)
	}
	defer file.Close()

	source := pipeline.ReaderSource(file, -1)
	for v := range source {
		fmt.Println(v)
	}
}

/**
将创建的pipeline, 写入到文件
*/
func writeToFile(p <-chan int, filename string) {
	file, err := os.Create(filename)
	if err != nil {
		panic(err)
	}
	defer file.Close()

	writer := bufio.NewWriter(file)
	defer writer.Flush()

	pipeline.WriteSlink(writer, p)
}

/**
@param filename 文件名
@param fileSize 文件大小
@param chunkCount 分块数量
*/
func createPipeline(filename string, fileSize, chunkCount int) <-chan int {

	// 大小/块数量=每块大小
	chunkSize := fileSize / chunkCount
	var sortResults []<-chan int

	pipeline.Init()

	for i := 0; i < chunkCount; i++ {

		file, err := os.Open(filename)
		if err != nil {
			panic(err)
		}

		// 当前第几块 * 每一块的大小
		// whence=0, 从第0位开始读
		file.Seek(int64(i*chunkSize), 0)

		// 读取文件指定长度
		source := pipeline.ReaderSource(bufio.NewReader(file), chunkSize)

		// 将文件内容排序后添加到结果中
		sortResults = append(sortResults, pipeline.InMemSort(source))
	}

	return pipeline.MergeN(sortResults...)
}
