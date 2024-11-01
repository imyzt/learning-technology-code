package main

import "fmt"

// 双指针， 双读单写指针 三指针
// 两个列表最后一个元素对比，谁大就把谁放在nums1的最后，然后挪动大的那个数组的读指针向头部靠近，写指针也向前移动
func merge(nums1 []int, m int, nums2 []int, n int) {
	if n == 0 {
		return
	}
	// rIdx1，rIdx2=nums1，2 的实数末尾读指针
	// wIdx=nums1的最后一位索引,为写指针
	rIdx1, rIdx2, wIdx := m-1, n-1, m+n-1
	for rIdx1 >= 0 && rIdx2 >= 0 {
		if nums1[rIdx1] > nums2[rIdx2] {
			nums1[wIdx] = nums1[rIdx1]
			rIdx1--
		} else {
			nums1[wIdx] = nums2[rIdx2]
			rIdx2--
		}
		wIdx--
	}
	for rIdx2 >= 0 {
		nums1[wIdx] = nums2[rIdx2]
		wIdx--
		rIdx2--
	}
	fmt.Println(nums1)
}
