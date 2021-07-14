package pipeline

import (
	"bufio"
	"net"
)

/**
向网络上发送数据
*/
func NetworkSink(addr string, in <-chan int) {

	listen, err := net.Listen("tcp", addr)
	if err != nil {
		panic(err)
	}

	go func() {

		defer listen.Close()

		// 只连接一次
		conn, err := listen.Accept()
		if err != nil {
			panic(err)
		}
		defer conn.Close()

		// 发送数据
		writer := bufio.NewWriter(conn)
		defer writer.Flush()

		// 向网络write写入数据
		WriteSlink(writer, in)
	}()
}

/**
连接网络, 接收网络数据, 保存到out
*/
func NetworkSource(addr string) chan int {
	out := make(chan int)
	go func() {
		conn, err := net.Dial("tcp", addr)
		if err != nil {
			panic(err)
		}
		source := ReaderSource(bufio.NewReader(conn), -1)
		for v := range source {
			out <- v
		}
		close(out)
	}()
	return out
}
