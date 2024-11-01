package main

func deleteDuplicates(head *ListNode) *ListNode {

	dummy := &ListNode{}
	curr := dummy
	curr.Next = head
	curr = curr.Next
	for ; head.Next != nil; head = head.Next {
		if head.Val != head.Next.Val {
			curr.Next = head.Next
			curr = curr.Next
		}
	}
	// 确保新链表的末尾指向 nil
	curr.Next = nil

	// 自己想的方案，空间复杂度O(n)
	//dummy := &ListNode{}
	//for curr := dummy; head != nil; head = head.Next {
	//	if head.Next == nil || head.Val != head.Next.Val {
	//		curr.Next = &ListNode{Val: head.Val}
	//		curr = curr.Next
	//	}
	//}
	//return dummy.Next
	return dummy.Next
}
