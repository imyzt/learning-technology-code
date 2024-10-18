package main

import (
	"fmt"
	"strings"
)

//合并两个已排序的列表，返回新的已排序的列表
func mergeTwoLists(list1 *ListNode, list2 *ListNode) *ListNode {
	if list1 == nil {
		return list2
	}
	if list2 == nil {
		return list1
	}
	dummy := &ListNode{}
	current := dummy
	p1, p2 := list1, list2
	for p1 != nil && p2 != nil {
		if p1.Val < p2.Val {
			current.Next = p1
			p1 = p1.Next
		} else {
			current.Next = p2
			p2 = p2.Next
		}
		current = current.Next
		fmt.Println(current)
	}
	if p1 != nil {
		current.Next = p1
	}
	if p2 != nil {
		current.Next = p2
	}
	return dummy.Next
}

type ListNode struct {
	Val  int
	Next *ListNode
}

// String 方法用于格式化输出链表
func (node *ListNode) String() string {
	if node == nil {
		return "nil"
	}
	var result []string
	for n := node; n != nil; n = n.Next {
		result = append(result, fmt.Sprintf("%d", n.Val))
	}
	return "[" + strings.Join(result, " -> ") + "]"
}

//You are given the heads of two sorted linked lists list1 and list2.
//
//Merge the two lists into one sorted list. The list should be made by splicing together the nodes of the first two lists.
//
//Return the head of the merged linked list.
//
//Example 1:
//
//
//Input: list1 = [1,2,4], list2 = [1,3,4]
//Output: [1,1,2,3,4,4]
//Example 2:
//
//Input: list1 = [], list2 = []
//Output: []
//Example 3:
//
//Input: list1 = [], list2 = [0]
//Output: [0]
//Constraints:
//
//The number of nodes in both lists is in the range [0, 50].
//-100 <= Node.val <= 100
//Both list1 and list2 are sorted in non-decreasing order.
//Related Topics
//Linked List
//Recursion
