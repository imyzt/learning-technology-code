package main

import "fmt"

func data() {

	arr := [10]int{}
	arr[0] = 12
	fmt.Printf("%v\n", arr)
	//[12 0 0 0 0 0 0 0 0 0]

	// 空集合
	slice := make([]int, 0, 10)
	slice = append(slice, 12)
	fmt.Printf("%v, 地址：%p\n", slice, &slice)
	slice = append(slice, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
	fmt.Printf("%v, 地址：%p\n", slice, &slice)
	fmt.Println(slice[:0])
	fmt.Println(slice[1:])
	//[]
	//[1 2 3 4 5 6 7 8 9 10]
	slice = append(slice[:0], slice[1:]...)
	fmt.Printf("%v, 地址：%p\n", slice, &slice)
	//[12], 地址：0xc00000c048
	//[12 1 2 3 4 5 6 7 8 9 10], 地址：0xc00000c048
	//[1 2 3 4 5 6 7 8 9 10], 地址：0xc00000c048
	newSlice := slice[0:]
	fmt.Printf("newSlice %v, 地址：%p\n", newSlice, &newSlice)
	//newSlice [1 2 3 4 5 6 7 8 9 10], 地址：0xc0000a40c0

	// 有零值的集合
	slice2 := make([]int, 10)
	slice2[0] = 12
	fmt.Printf("%v\n", slice2)
	//[12 0 0 0 0 0 0 0 0 0]

	arr2 := new([10]int)
	arr2[0] = 12
	fmt.Printf("%v\n", *arr2)
	//[12 0 0 0 0 0 0 0 0 0]

}
