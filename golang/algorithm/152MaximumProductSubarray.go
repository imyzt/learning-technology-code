package main

//func maxProduct(nums []int) int {
//	if len(nums) == 1 {
//		return nums[0]
//	}
//	maxProduct := nums[0]
//	for i := 0; i < len(nums); i++ {
//		product := nums[i]
//		maxProduct = max(maxProduct, product)
//		for j := i + 1; j < len(nums); j++ {
//			product = product * nums[j]
//			maxProduct = max(maxProduct, product)
//		}
//	}
//	return maxProduct
//}

func maxProduct(nums []int) int {
	if len(nums) == 0 {
		return 0
	}
	ans, maxVal, minVal := nums[0], nums[0], nums[0]
	for _, num := range nums[1:] {
		if num < 0 {
			// 如果当前值为负数，交换 maxVal 和 minVal，因为乘以负数最大最小值会反转
			maxVal, minVal = minVal, maxVal
		}
		maxVal = max(num, maxVal*num)
		minVal = min(num, minVal*num)
		ans = max(maxVal, ans)
	}
	return ans
}
