package main

// 是否存在重复元素
func containsNearbyDuplicate(nums []int, k int) bool {
	if len(nums) <= 1 {
		return false
	}

	// hash表+暴力破解
	//seen := make(map[int]int)
	//for i := range nums {
	//	if j, ok := seen[nums[i]]; ok && int(math.Abs(float64(j-i))) <= k {
	//		return true
	//	}
	//	seen[nums[i]] = i
	//}

	// 滑动窗口
	window := make(map[int]bool)
	for i, num := range nums {
		if window[num] {
			// 如果存在就返回
			return true
		}
		// 填入当前值
		window[num] = true
		if i >= k {
			// 如果窗口大小超过 k，移除最早进入窗口的元素
			delete(window, nums[i-k])
		}
	}
	return false
}
