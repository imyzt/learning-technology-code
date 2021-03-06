package main

import (
	"bufio"
	"fmt"
	"golang-imooc-distributed-computing/pipeline"
	"os"
	"strconv"
)

func main() {

	// 本地版本
	//p := createPipeline("large.in", 800000000, 4)
	//writeToFile(p, "large.out")
	//printFile("large.out")

	// 网络版本
	p := createNetworkPipeline("large.in", 800000000, 4)
	writeToFile(p, "large.out")
	printFile("large.out")
}

func printFile(filename string) {
	file, err := os.Open(filename)
	if err != nil {
		panic(err)
	}
	defer file.Close()

	source := pipeline.ReaderSource(file, -1)
	count := 0
	for v := range source {
		fmt.Println(v)
		count++
		if count > 100 {
			break
		}
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
本地版本
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

/**
网络版本
@param filename 文件名
@param fileSize 文件大小
@param chunkCount 分块数量
*/
func createNetworkPipeline(filename string, fileSize, chunkCount int) <-chan int {

	// 大小/块数量=每块大小
	chunkSize := fileSize / chunkCount
	var sortAddr []string

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

		// 地址随着块id变化
		addr := ":" + strconv.Itoa(7000+i)
		// 创建一个server, 向网络发送排序后的数据
		pipeline.NetworkSink(addr,
			// 排序后
			pipeline.InMemSort(source))

		sortAddr = append(sortAddr, addr)
	}

	// 创建N[根据块index]个client, 从网络上接收server数据, 放到sortResults中
	var sortResults []<-chan int
	for _, addr := range sortAddr {
		sortResults = append(sortResults, pipeline.NetworkSource(addr))
	}

	return pipeline.MergeN(sortResults...)
}
