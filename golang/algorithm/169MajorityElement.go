package main

import "fmt"

// 摩尔投票, 时间复杂度O(n), 空间复杂度O(1) -> 只适用于一定有众数的情况,否则不准
// hash表, 时间复杂度O(n), 空间复杂度O(n)
// 排序, 取中位数, 时间复杂度O(nLogn), 空间复杂度 不需额外存储
func majorityElement(nums []int) int {
	candidate, count := 0, 0
	for i := range nums {
		if count == 0 {
			// 初始的情况或投票数被抵消重新计数, 需要将选手设定为当前元素, 票数设为1
			candidate = nums[i]
			count = 1
			fmt.Printf("%d参选, 当前票: %d\n", candidate, count)
			continue
		}
		if nums[i] == candidate {
			// 当前元素相等就加票
			count++
			fmt.Printf("%d加一票, 当前票: %d\n", candidate, count)
		} else {
			// 否则抵消一个票
			count--
			fmt.Printf("%d减一票, 当前票: %d\n", candidate, count)
		}
	}
	//3参选, 当前票: 1
	//3减一票, 当前票: 0
	//3参选, 当前票: 1
	//3减一票, 当前票: 0
	//3参选, 当前票: 1
	return candidate
}
