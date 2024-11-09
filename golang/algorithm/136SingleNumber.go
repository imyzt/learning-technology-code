package main

// 给定一个整数nums的非空数组，除一个元素外，每个元素出现两次。找到那一个。
// 您必须实现具有线性运行时复杂度的解决方案，并且只使用恒定的额外空间。
func singleNumber(nums []int) int {
	res := 0
	for _, num := range nums {
		// 任何数字与自身异或的结果为0（a ^ a = 0），任何数字与0异或的结果为其自身（a ^ 0 = a）。
		res = res ^ num
	}
	return res

	// 简单理解版,需要额外的存储空间
	//numMap := make(map[int]int, len(nums)/2)
	//res := 0
	//for _, num := range nums {
	//	numMap[num]++
	//	if numMap[num] == 1 {
	//		res += num
	//	} else {
	//		res -= num
	//	}
	//}
	//return res
}
