package main

// 给定一个整数nums的非空数组，除一个元素外，每个元素出现两次。找到那一个。
// 您必须实现具有线性运行时复杂度的解决方案，并且只使用恒定的额外空间。
func singleNumber2(nums []int) int {
	numMap := make(map[int]int, len(nums)/2)
	res := 0
	for _, num := range nums {
		numMap[num]++
		if numMap[num] <= 2 {
			res += num
		} else {
			res -= num * 2
		}
	}
	return res
}
