package main

func maxDepth(root *TreeNode) int {
	if root == nil {
		return 0
	}
	lDepth, rDepth := maxDepth(root.Left), maxDepth(root.Right)
	return max(lDepth, rDepth) + 1
}
func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}
