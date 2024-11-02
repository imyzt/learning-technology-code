package main

// 镜像对称二叉树
func isSymmetric(root *TreeNode) bool {
	return root == nil || isSymmetric1(root.Left, root.Right)
}

func isSymmetric1(left *TreeNode, right *TreeNode) bool {
	// 是否存在一边为空的情况,如果有一边为空另一边不是则返回false
	if left == nil || right == nil {
		return left == nil && right == nil
	}
	// 对比两个相对位置是否值相等
	if left.Val != right.Val {
		return false
	}
	// 左树的左节点和右树的右节点, 左树的右节点和右树的左节点进行递归对比
	return isSymmetric1(left.Left, right.Right) && isSymmetric1(left.Right, right.Left)
}

//    1
//   / \
//  2   2
// / \ / \
//3  4 4  3
