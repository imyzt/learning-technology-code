package main

func getIntersectionNode(headA, headB *ListNode) *ListNode {

	//p1, p2 := headA, headB
	for headA != nil || headB != nil {
		if headA != nil {
			if headA != headB {
				headA = headA.Next
			}
		}
		if headB != nil {
			if headB != headA {
				headB = headB.Next
			}
		}
	}
	return headA
}

// 创建一个新节点
func createListNode(val int) *ListNode {
	return &ListNode{Val: val, Next: nil}
}

// 创建链表 1: A1 → A2 → A3 → A4 → A5
func createList1() *ListNode {
	A1 := createListNode(1)
	A2 := createListNode(2)
	A3 := createListNode(3)
	A4 := createListNode(4)
	A5 := createListNode(5)

	A1.Next = A2
	A2.Next = A3
	A3.Next = A4
	A4.Next = A5

	return A1
}

// 创建链表 2: B1 → B2 → B3 → Intersection → A4 → A5
func createList2() *ListNode {
	B1 := createListNode(1)
	B2 := createListNode(2)
	B3 := createListNode(3)
	A4 := createListNode(4) // 这个节点是两个链表共享的，即交点
	A5 := createListNode(5)

	B1.Next = B2
	B2.Next = B3
	B3.Next = A4 // 设置交点
	A4.Next = A5

	return B1
}
