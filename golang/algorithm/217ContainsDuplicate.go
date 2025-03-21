package main

import "sort"

// 是否存在重复元素
func containsDuplicate(nums []int) bool {
	if len(nums) <= 1 {
		return false
	}
	// 空间换时间
	//seen := make(map[int]bool, len(nums))
	//for i := range nums {
	//	if seen[nums[i]] {
	//		return true
	//	}
	//	seen[nums[i]] = true
	//}
	//return false

	// 不需要额外空间
	sort.Slice(nums, func(i, j int) bool {
		return nums[i] < nums[j]
	})
	for i := 0; i < len(nums)-1; i++ {
		if nums[i] == nums[i+1] {
			return true
		}
	}
	return false
}
