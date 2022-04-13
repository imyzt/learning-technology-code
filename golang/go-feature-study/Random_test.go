package main

import (
	"encoding/hex"
	"fmt"
	"math/rand"
	"testing"
	"time"
)

func init() {
	rand.Seed(time.Now().Unix())
}

func RandStr1(n int) string {
	bytes := []byte("ABCDEFGHIGKLMNOPQRSTUVWXYZabcdefghigklmnopqrstuvwxyz")
	result := make([]byte, n)
	for i := 0; i < n; i++ {
		result[i] = bytes[rand.Int31()%int32(len(bytes))]
	}
	return string(result)
}

func RandStr2(n int) string {
	// 一个byte最大值是0xFF,转化成字符串时就是两个, 所以除2
	result := make([]byte, n/2)
	rand.Read(result)
	return hex.EncodeToString(result)
}

func Test_1(t *testing.T) {
	fmt.Println(RandStr1(32))
	fmt.Println(RandStr2(32))
}

func Benchmark_1(b *testing.B) {
	b.RunParallel(func(p *testing.PB) {
		for p.Next() {
			RandStr1(32)
		}
	})
}

func Benchmark_2(b *testing.B) {
	b.RunParallel(func(p *testing.PB) {
		for p.Next() {
			RandStr2(32)
		}
	})
}
