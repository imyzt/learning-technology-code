package main

import "fmt"

// 中序遍历二叉树：先打印左边，再打印自己，再打印右边
func inorderTraversal(root *TreeNode) []int {
	//	左侧还有，就一直访问左侧，直至无左节点，打印自己，再打印右侧，再打印上级
	return *inorder(root, &[]int{})
}

func inorder(node *TreeNode, result *[]int) *[]int {
	if node == nil {
		return result
	}
	inorder(node.Left, result)
	*result = append(*result, node.Val)
	inorder(node.Right, result)
	return result
}

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

// printTree 前序遍历打印二叉树
func printTree(node *TreeNode) {
	if node == nil {
		return
	}
	fmt.Printf("%d ", node.Val)
	printTree(node.Left)
	printTree(node.Right)
}
