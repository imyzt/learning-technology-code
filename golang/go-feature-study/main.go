package main

import "fmt"

func main1() {

	// 一般情况下, 流应该由生产端关闭
	// 常规消费端关闭流, 生产端直接异常
	// normalChannelCloseErr()
	// 优雅关闭流(类似于发送信号量)
	// graceChannelClose()

	// 泛型
	fmt.Println(min(1, 2))
	fmt.Println(min(1.1, 2.2))
	fmt.Println(min("3", "2"))
	// 自定义类型泛型
	num1 := myStr("33")
	num2 := myStr("344")
	fmt.Println(min(num1, num2))
}
