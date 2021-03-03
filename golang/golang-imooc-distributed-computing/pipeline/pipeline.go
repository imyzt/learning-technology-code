package pipeline

import (
	"encoding/binary"
	"fmt"
	"io"
	"math/rand"
	"sort"
	"time"
)

var (
	startTime time.Time
)

func Init() {
	startTime = time.Now()
}

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
	out := make(chan int, 1024)
	go func() {
		// Read into Memory
		var arr []int
		for v := range source {
			arr = append(arr, v)
		}
		fmt.Println("Read done: ", time.Now().Sub(startTime))

		// Sort
		sort.Ints(arr)
		fmt.Println("InMemSort done: ", time.Now().Sub(startTime))

		// Output
		for _, v := range arr {
			out <- v
		}
		close(out)
	}()
	return out
}

func Merge(in1, in2 <-chan int) <-chan int {
	out := make(chan int, 1024)
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
		fmt.Println("Merge done: ", time.Now().Sub(startTime))
	}()
	return out
}

/**
@param reader 读文件流
@param chunkSize 块大小
*/
func ReaderSource(reader io.Reader, chunkSize int) <-chan int {
	out := make(chan int, 1024)
	go func() {
		buffer := make([]byte, 8)
		bytesRead := 0
		for {
			n, err := reader.Read(buffer)
			if n > 0 {
				v := int(binary.BigEndian.Uint64(buffer))
				out <- v
			}
			bytesRead += n
			if err != nil ||
				// 不等于-1(-1表示读完所有) 并且 当前读的文件>=所有块大小(相当于读完文件)
				(chunkSize != -1 && bytesRead >= chunkSize) {
				break
			}
		}
		close(out)
	}()
	return out
}

func WriteSlink(writer io.Writer, in <-chan int) {
	for v := range in {
		buffer := make([]byte, 8)
		binary.BigEndian.PutUint64(buffer, uint64(v))
		_, _ = writer.Write(buffer)
	}
}

func RandomSource(count int) <-chan int {
	out := make(chan int)
	go func() {
		for i := 0; i < count; i++ {
			out <- rand.Int()
		}
		close(out)
	}()
	return out
}

func MergeN(inputs ...<-chan int) <-chan int {
	if len(inputs) == 1 {
		return inputs[0]
	}
	m := len(inputs) / 2
	// Merge inputs[0..m) and inputs[m...end)
	return Merge(MergeN(inputs[:m]...), MergeN(inputs[m:]...))
}
