package main

// 判断两个二叉树是否完全相等
func isSameTree(p *TreeNode, q *TreeNode) bool {
	return isSameTree1(p, q)
}

func isSameTree1(p *TreeNode, q *TreeNode) bool {
	if p == nil && q == nil {
		return true
	}
	if p == nil || q == nil {
		return false
	}
	if p.Val != q.Val {
		return false
	}
	return isSameTree1(p.Left, q.Left) && isSameTree1(p.Right, q.Right)
}
