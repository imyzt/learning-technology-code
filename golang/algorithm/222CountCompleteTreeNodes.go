package main

func countNodes(root *TreeNode) int {

	// 普通方案O(n)
	//var num int
	//return countNodes2(root, &num)

	// 完全二叉树可以 log2(n) 计算
	// 满二叉树的数量=高度*2， 可以减少满二叉树的遍历
	if root == nil {
		return 0
	}
	lHeight := getHeight(root.Left)
	rHeight := getHeight(root.Right)
	if lHeight == rHeight {
		// 如果左右高度相同，可以确定左子树是满二叉树
		return (1 << lHeight) + countNodes(root.Right)
	} else {
		// 否则，右子树是满二叉树
		return (1 << rHeight) + countNodes(root.Left)
	}
}

func getHeight(node *TreeNode) int {
	height := 0
	for node != nil {
		height++
		node = node.Left
	}
	return height
}

func countNodes2(node *TreeNode, count *int) int {
	if node == nil {
		return 0
	}
	*count++
	if node.Left == nil && node.Right == nil {
		return *count
	}
	countNodes2(node.Left, count)
	countNodes2(node.Right, count)
	return *count
}

//    1
//   /
//  2
// / \
//3  4
