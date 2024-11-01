package main

func isSymmetric(root *TreeNode) bool {
	return root == nil || isSymmetric1(root.Left, root.Right)
}

// 镜像对称二叉树
func isSymmetric1(p *TreeNode, q *TreeNode) bool {
	if p == nil && q == nil {
		return true
	}
	if p == nil || q == nil {
		return false
	}
	if p.Val != q.Val {
		return false
	}
	return isSymmetric1(p.Left, q.Right) && isSymmetric1(p.Right, q.Left)
}

//    1
//   / \
//  2   2
// / \ / \
//3  4 4  3
