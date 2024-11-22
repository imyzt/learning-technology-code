package main

import "fmt"

func isHappy(n int) bool {

	// hash表存储去重方案
	//seen := make(map[int]bool)
	//for n != 0 && !seen[n] {
	//	// 计算每一位的平方和
	//	seen[n] = true
	//	tmp := n
	//	n = getSum(n)
	//	fmt.Printf("happy, %d的平方和=%d\n", tmp, n)
	//}
	//return n == 1

	// 快慢指针方案
	slow, fast := n, getSum(n)
	for slow != fast {
		slow = getSum(slow)
		fast = getSum(getSum(fast))
		fmt.Printf("slow=%d,fast=%d\n", slow, fast)
	}
	return slow == 1
}

func getSum(n int) int {
	res := 0
	for n > 0 {
		num := n % 10
		n /= 10
		res += num * num
	}
	return res
}
