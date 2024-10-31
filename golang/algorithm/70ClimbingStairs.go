package main

import "fmt"

// 爬楼梯经典问题，动态规划，状态转移方程
// 任何一级台阶(i)可以走的路线都是由其后一级(i-1)和后两级(i-2)台阶的路线累加所得
func climbStairs(n int) int {
	if n <= 2 {
		return n
	}
	// 简版，易于理解，空间复杂度O(n)
	//dp := make([]int, n+1)
	//dp[1], dp[2] = 1, 2
	//for i := 3; i <= n; i++ {
	//	dp[i] = dp[i-1] + dp[i-2]
	//}
	//return dp[n]

	// 复杂版，空间复杂度O(1)
	p1, p2 := 1, 2
	for i := 3; i <= n; i++ {
		p1, p2 = p2, p1+p2
		fmt.Printf("第%d阶，%d种方案\n", i, p2)
	}
	return p2
}

//You are climbing a staircase. It takes n steps to reach the top.
//
//Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
//
//Example 1:
//
//Input: n = 2
//Output: 2
//Explanation: There are two ways to climb to the top.
//1. 1 step + 1 step
//2. 2 steps
//Example 2:
//
//Input: n = 3
//Output: 3
//Explanation: There are three ways to climb to the top.
//1. 1 step + 1 step + 1 step
//2. 1 step + 2 steps
//3. 2 steps + 1 step
//Constraints:
//
//1 <= n <= 45
//Related Topics
//Math
//Dynamic Programming
//Memoization
