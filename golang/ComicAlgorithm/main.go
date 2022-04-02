package main

import (
	"comicAlgorithm/collutil"
	"fmt"
	"math/rand"
)

/**
广度优先算法遍历二叉树
*/
func main() {

	// 初始化二叉树
	treeNode := collutil.NewTreeNode(rand.Int() / 218430245590244363)

	for i := 1; i < 10; i++ {
		treeNode.Insert(rand.Int() / 218430245590244363)
	}

	queue := collutil.NewQueue()

	queue.Offer(*treeNode)

	for !queue.IsEmpty() {

		element := queue.Poll()

		fmt.Println(element.Value)

		if element.Left != nil {
			queue.Offer(*element.Left)
		}
		if element.Right != nil {
			queue.Offer(*element.Right)
		}

	}
}
