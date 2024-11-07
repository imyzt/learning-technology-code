package main

//最长连续序列

func longestConsecutive(nums []int) int {
	if len(nums) == 0 {
		return 0
	}
	numMap := make(map[int]bool, len(nums))
	for i := range nums {
		numMap[nums[i]] = true
	}
	maxStreak := 0
	for num := range numMap {
		// 每个数都尝试-1，如果存在map中就说明自身不是序列的起点，自己只可能是序列的非首元素
		if !numMap[num-1] {
			currNum := num
			currStreak := 1
			// 在当前基础上数字累加，一直for循环判断下一个序列是否存在map中
			for numMap[currNum+1] {
				currNum++
				currStreak++
			}
			maxStreak = max(maxStreak, currStreak)
		}
	}
	return maxStreak
}

//给定一个未排序的整数数组 nums，找出最长连续序列的长度。
//要求算法的时间复杂度为 O(n)。
//输入: nums = [100, 4, 200, 1, 3, 2]
//输出: 4
//解释: 最长连续序列是 [1, 2, 3, 4]。它的长度为 4。
