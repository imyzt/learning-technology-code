package main

import "math"

func minDepth(root *TreeNode) int {
	if root == nil {
		return 0
	}
	return minDepth1(root, 0)
}
func minDepth1(node *TreeNode, height int) int {
	if node == nil {
		return math.MaxInt32
	}
	if node.Left == nil && node.Right == nil {
		return 1
	}
	leftHeight := minDepth1(node.Left, height+1)
	rightHeight := minDepth1(node.Right, height+1)
	return 1 + min(leftHeight, rightHeight)
}

func min(a, b int) int {
	if a > b {
		return b
	}
	return a
}
