package main

// 链表,进位运算
func addTwoNumbers(l1 *ListNode, l2 *ListNode) *ListNode {

	// 从个位开始累加,加到不能加为止,有进位(carry>0)则加一
	carry, p1, p2 := 0, l1, l2
	result := &ListNode{}
	curr := result
	for p1 != nil || p2 != nil || carry > 0 {
		sum := carry
		if p1 != nil {
			sum += p1.Val
			p1 = p1.Next
		}
		if p2 != nil {
			sum += p2.Val
			p2 = p2.Next
		}
		carry = sum / 10
		curr.Next = &ListNode{
			Val:  sum % 10,
			Next: nil,
		}
		curr = curr.Next
	}
	return result.Next
}

//You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
//
//You may assume the two numbers do not contain any leading zero, except the number 0 itself.
//
//
//
//Example 1:
//
//
//Input: l1 = [2,4,3], l2 = [5,6,4]
//Output: [7,0,8]
//Explanation: 342 + 465 = 807.
//Example 2:
//
//Input: l1 = [0], l2 = [0]
//Output: [0]
//Example 3:
//
//Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
//Output: [8,9,9,9,0,0,0,1]
//
//
//Constraints:
//
//The number of nodes in each linked list is in the range [1, 100].
//0 <= Node.val <= 9
//It is guaranteed that the list represents a number that does not have leading zeros.
