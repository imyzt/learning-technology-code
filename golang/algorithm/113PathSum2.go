package main

// 路径总和二，找到所有符合条件的路径
// 找到所有路径相加总和=targetSum的路径
func pathSum(root *TreeNode, targetSum int) [][]int {
	var result [][]int
	pathSum1(root, targetSum, &[]int{}, &result)
	return result
}

func pathSum1(node *TreeNode, targetSum int, path *[]int, result *[][]int) {
	if node == nil {
		return
	}
	// 添加当前节点到路径中
	*path = append(*path, node.Val)
	// 叶子节点，且当前路径符合题目要求，添加到结果中
	if node.Left == nil && node.Right == nil && targetSum-node.Val == 0 {
		*result = append(*result, append([]int(nil), *path...))
	}
	pathSum1(node.Left, targetSum-node.Val, path, result)
	pathSum1(node.Right, targetSum-node.Val, path, result)

	*path = (*path)[:len(*path)-1]
}

//    5
//   / \
//  4   8
// /   / \
//11  13  4
// /  \    \
//7    2    5
