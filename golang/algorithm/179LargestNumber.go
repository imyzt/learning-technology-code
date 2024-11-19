package main

import (
	"sort"
	"strconv"
)

// 关键点： 排序， 比较ab和ba谁大
//输入：nums = [3, 30, 34, 5, 9]
//输出："9534330"
//输入：nums = [10, 2]
//输出："210"
func largestNumber(nums []int) string {

	// 内置高效排序
	sort.Slice(nums, func(i, j int) bool {
		x := strconv.Itoa(nums[i]) + strconv.Itoa(nums[j])
		y := strconv.Itoa(nums[j]) + strconv.Itoa(nums[i])
		return x > y
	})

	// O(n²)手动排序
	//for i := 0; i < len(nums); i++ {
	//	for j := i + 1; j < len(nums); j++ {
	//		x := strconv.Itoa(nums[i]) + strconv.Itoa(nums[j])
	//		y := strconv.Itoa(nums[j]) + strconv.Itoa(nums[i])
	//		if x < y {
	//			nums[i], nums[j] = nums[j], nums[i]
	//		}
	//	}
	//}

	res := ""
	for k := range nums {
		res += strconv.Itoa(nums[k])
	}
	if res[0] == '0' {
		return "0"
	}
	return res
}
