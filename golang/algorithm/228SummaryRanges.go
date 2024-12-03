package main

import "strconv"

func summaryRanges(nums []int) []string {
	var res []string
	if len(nums) == 0 {
		return res
	}

	appendStr := func(start, end int, res *[]string) {
		if start == end {
			*res = append(*res, strconv.Itoa(start))
		} else {
			*res = append(*res, strconv.Itoa(start)+"->"+strconv.Itoa(end))
		}
	}

	start := nums[0]
	for i := 1; i < len(nums); i++ {
		if nums[i] != nums[i-1]+1 {
			appendStr(start, nums[i-1], &res)
			start = nums[i]
		}
	}
	appendStr(start, nums[len(nums)-1], &res)
	return res
}
