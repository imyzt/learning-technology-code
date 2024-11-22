package main

// 移除链表指定元素
// 关键在于，将链表的next如何接上，需要操作 curr.Next 便于拼接， 如果直接操作curr，无法知道curr的上一个元素，没办法跳过curr
func removeElements(head *ListNode, val int) *ListNode {
	if head == nil {
		return nil
	}
	// 创建虚拟头节点，因为操作的是curr.next，所以真实元素是从curr.next开始的
	dummy := &ListNode{Next: head}
	curr := dummy
	for curr.Next != nil {
		if curr.Next.Val == val {
			// 如果next的值需要删除，则将next.next拼接到next上
			curr.Next = curr.Next.Next
		} else {
			// 往后走
			curr = curr.Next
		}
	}
	return dummy.Next
}
