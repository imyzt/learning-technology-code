package main

// 遍历二叉树
func preorderTraversal(root *TreeNode) []int {
	res := new([]int)
	preorderTraversal2(root, res)
	return *res
}
func preorderTraversal2(node *TreeNode, res *[]int) {
	if node != nil {
		*res = append(*res, node.Val)
		preorderTraversal2(node.Left, res)
		preorderTraversal2(node.Right, res)
	}
}
