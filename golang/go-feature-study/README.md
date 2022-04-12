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