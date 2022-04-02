package collutil

type Queue struct {
	value []TreeNode
}

func NewQueue() *Queue {
	return &Queue{make([]TreeNode, 0)}
}

func (queue *Queue) Offer(v TreeNode) {
	queue.value = append(queue.value, v)
}

func (queue *Queue) Poll() TreeNode {
	var x TreeNode
	x, queue.value = queue.value[0], queue.value[1:]
	return x
}

func (queue *Queue) IsEmpty() bool {
	return len(queue.value) == 0
}
