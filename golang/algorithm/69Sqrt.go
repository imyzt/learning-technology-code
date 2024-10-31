package main

// 二分查找找到最合适的数值，
func mySqrt(x int) int {
	if x == 0 {
		return 0
	}
	left, right := 0, x
	for left <= right {
		mid := left + (right-left)/2
		if mid*mid == x {
			return mid
		} else if mid*mid < x {
			left = mid + 1
		} else {
			right = mid - 1
		}
	}
	return right
}
