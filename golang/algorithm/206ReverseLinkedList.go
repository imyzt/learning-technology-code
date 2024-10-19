package main

func reverse(pre, curr *ListNode) *ListNode {
	if curr == nil {
		return pre
	}
	next := curr.Next
	curr.Next = pre
	return reverse(curr, next)
}

func reverseList(head *ListNode) *ListNode {
	if head == nil {
		return nil
	}
	curr := head
	var pre *ListNode

	// 方案1, 双指针法
	//for curr != nil {
	//	next := curr.Next
	//	// 翻转链表的方向
	//	curr.Next = pre
	//	// 向前进一位
	//	pre = curr
	//	curr = next
	//}
	//return pre

	// 方案2, 递归
	return reverse(pre, curr)
}

//Given the head of a singly linked list, reverse the list, and return the reversed list.
//
//
//
//Example 1:
//
//
//Input: head = [1,2,3,4,5]
//Output: [5,4,3,2,1]
//Example 2:
//
//
//Input: head = [1,2]
//Output: [2,1]
//Example 3:
//
//Input: head = []
//Output: []
//
//
//Constraints:
//
//The number of nodes in the list is the range [0, 5000].
//-5000 <= Node.val <= 5000
//
//
//Follow up: A linked list can be reversed either iteratively or recursively. Could you implement both?
