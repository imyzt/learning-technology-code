package main

import "sort"

// 是否存在重复元素
func containsDuplicate(nums []int) bool {
	if len(nums) <= 1 {
		return false
	}
	// 空间换时间
	v := make(map[int]bool, len(nums))
	for i := range nums {
		if _, ok := v[nums[i]]; ok {
			return true
		}
		v[nums[i]] = true
	}

	sort.Slice(nums, func(i, j int) bool {
		return i > j
	})
	i, j := 0, 1
	for i < j {
		if nums[i] == nums[j] {
			return true
		}
		i++
	}

	return false
}
