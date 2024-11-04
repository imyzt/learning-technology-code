package main

import "math"

// 判断是否是完全平衡二叉树
func isBalanced(root *TreeNode) bool {
	_, balanced := checkBalanced(root)
	return balanced
}

func checkBalanced(root *TreeNode) (int, bool) {
	if root == nil {
		return 0, true
	}
	leftHeight, leftBalanced := checkBalanced(root.Left)
	rightHeight, rightBalanced := checkBalanced(root.Right)
	// 当前高度
	currHeight := max(leftHeight, rightHeight) + 1
	// 当前是否平衡（和上一层级是否差值>1）
	currBalanced := leftBalanced && rightBalanced && math.Abs(float64(leftHeight-rightHeight)) <= 1
	return currHeight, currBalanced
}
