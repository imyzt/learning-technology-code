package main

//给定一个二叉树，你需要将这个二叉树翻转，使得原树中左子树的节点成为新树的右子树，
//原树中右子树的节点成为新树的左子树，并且这一变换需要递归地应用于整棵树。

func revertBinaryTree(root *TreeNode) *TreeNode {
	if root == nil {
		return root
	}
	if root.Left == nil && root.Right == nil {
		return root
	}
	// 交换左右
	root.Left, root.Right = root.Right, root.Left
	// 遍历左子树
	revertBinaryTree(root.Left)
	// 遍历右子树
	revertBinaryTree(root.Right)
	return root
}
