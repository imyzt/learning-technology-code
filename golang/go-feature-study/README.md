# chanClose

优雅关闭流, 如果有消费者关闭流的需求, 可以通过定义一个类似信号量的通道来实现消费者到生产者的通信.

# 泛型
1. 1.18才有
2. 只能用在方法上,无法使用在结构体上
3. 泛型下边界需要在类型上使用 `~` 表达式
```

type myStr string

type customType interface {
	float32 | float64 | int | int8 | int32 | int16 | int64 | uint | uint8 | uint32 | uint16 | uint64 | ~string
}

func min[T customType](a, b T) T {
	if a > b {
		return b
	}
	return a
}

func main() {

    // 泛型
    fmt.Println(min(1, 2))
    fmt.Println(min(1.1, 2.2))
    fmt.Println(min("3", "2"))
    
    // 自定义类型泛型
    num1 := myStr("33")
    num2 := myStr("344")
    fmt.Println(min(num1, num2))
    
}
```

# golang 随机字符串

两种方案, rand.Read方案优于手写
```go
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
```

# 单元测试

1. 类必须是 `_test` 结尾
2. func Test_xxx 作为单元测试名称
3. func Benchmark_xxx 作为基准测试名称