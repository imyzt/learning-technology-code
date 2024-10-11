package main

import "fmt"

func basicType() {

	a := 10
	fmt.Println(a)
	//10

	// 将aPointer指向a的内存地址，这块地址不管怎么变都能通过aPointer访问到a的值
	aPointer := &a
	fmt.Println(&a)
	fmt.Println(aPointer)
	fmt.Println(*aPointer)
	//0xc00001e108 -> a变量值的内存地址
	//0xc00001e108 -> a变量值的内存地址
	//10 -> 通过访问aPointer的指针，访问a变量的值

	a = 20
	fmt.Println(&a)
	fmt.Println(aPointer)
	fmt.Println(*aPointer)
	//0xc00001e108 -> 内存地址没变
	//0xc00001e108 -> 内存地址没变
	//20  -> 通过访问aPointer的指针，访问a变量的值时， 值已改变

	fmt.Println(&aPointer)
}
