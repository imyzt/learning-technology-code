package main

// 从根节点到叶子节点，每条路径都组合成为数字，累加得到结果
func sumNumbers(root *TreeNode) int {
	return sumNum(root, 0)
}
func sumNum(node *TreeNode, sum int) int {
	if node == nil {
		return 0
	}
	sum = sum*10 + node.Val
	if node.Left == nil && node.Right == nil {
		return sum
	}
	leftSum := sumNum(node.Left, sum)
	rightSum := sumNum(node.Right, sum)
	return leftSum + rightSum
}
