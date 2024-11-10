package main

// 从根部往上遍历写入
func postorderTraversal(root *TreeNode) []int {
	res := new([]int)
	postorderTraversal2(root, res)
	return *res
}

func postorderTraversal2(node *TreeNode, res *[]int) {
	if node != nil {
		if node.Left == nil && node.Right == nil {
			// 写入叶子节点
			*res = append(*res, node.Val)
			return
		}
		postorderTraversal2(node.Left, res)
		postorderTraversal2(node.Right, res)
		// 两边都写入完, 写入自己
		*res = append(*res, node.Val)
	}
}
