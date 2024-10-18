package main

import "fmt"

// 二分查找
func searchInsert(nums []int, target int) int {
	low, high := 0, len(nums)-1
	for low <= high {
		mid := low + (high-low)/2
		if target > nums[mid] {
			low = mid + 1
		} else if target < nums[mid] {
			high = mid - 1
		} else {
			return mid
		}
	}
	fmt.Printf("%v, target=%d, insertIdx=", nums, target)
	return low
}

//Given a sorted array of distinct integers and a target value, return the
//index if the target is found. If not, return the index where it would be if it were
//inserted in order.
//
// You must write an algorithm with O(log n) runtime complexity.
//
//
// Example 1:
//
//
//Input: nums = [1,3,5,6], target = 5
//Output: 2
//
//
// Example 2:
//
//
//Input: nums = [1,3,5,6], target = 2
//Output: 1
//
//
// Example 3:
//
//
//Input: nums = [1,3,5,6], target = 7
//Output: 4
//
//
//
// Constraints:
//
//
// 1 <= nums.length <= 10⁴
// -10⁴ <= nums[i] <= 10⁴
// nums contains distinct values sorted in ascending order.
// -10⁴ <= target <= 10⁴
//
//
// Related Topics Array Binary Search 👍 16537 👎 765
