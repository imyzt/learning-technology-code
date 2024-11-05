package main

// 路径总和，找到是否有符合条件的路径
// 是否存在某一条路径相加总和=targetSum
func hasPathSum(root *TreeNode, targetSum int) bool {
	if root == nil {
		return false
	}
	if root.Left == nil && root.Right == nil {
		// 到达叶子节点， 判断剩余目标值与叶子节点的值是否相等
		return targetSum == root.Val
	}
	// 算左右两个节点， 每次都减去当前节点的值
	return hasPathSum(root.Left, targetSum-root.Val) || hasPathSum(root.Right, targetSum-root.Val)
}
