package collutil

type TreeNode struct {
	Value int
	Left  *TreeNode
	Right *TreeNode
}

func NewTreeNode(root int) *TreeNode {
	return &TreeNode{root, nil, nil}
}

func (node *TreeNode) Insert(v int) {
	if v < node.Value {
		if node.Left != nil {
			node.Left.Insert(v)
		} else {
			node.Left = &TreeNode{v, nil, nil}
		}
	} else {
		if node.Right != nil {
			node.Right.Insert(v)
		} else {
			node.Right = &TreeNode{v, nil, nil}
		}
	}
}
