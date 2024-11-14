package main

func sortedArrayToBST(nums []int) *TreeNode {
	if len(nums) == 0 {
		return nil
	}
	mid := len(nums) / 2
	res := &TreeNode{Val: nums[mid]}
	res.Left = sortedArrayToBST(nums[:mid])
	res.Right = sortedArrayToBST(nums[mid+1:])
	return res
}
