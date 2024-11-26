package main

func countPrimes(n int) int {
	if n <= 2 {
		return 0
	}

	// 埃拉托斯特尼筛法 O(n log log n)
	isPrime := make([]bool, n)
	// 初始化，假定所有数都是质数
	for i := 2; i < n; i++ {
		isPrime[i] = true
	}
	// 只循环到 √n
	for i := 2; i*i < n; i++ {
		// 如果当前数是质数
		if isPrime[i] {
			// 则将所有i的倍数都标记为不是质数
			for j := i * i; j < n; j += i {
				isPrime[j] = false
			}
		}
	}
	res := 0
	for i := range isPrime {
		if isPrime[i] == true {
			res++
		}
	}
	return res

	// 暴力破解，时间太长
	//res := 0
	//for i := 2; i < n; i++ {
	//	flag := true
	//	for j := 2; j < i; j++ {
	//		if i%j == 0 {
	//			flag = false
	//			break
	//		}
	//	}
	//	if flag {
	//		res++
	//	}
	//}
	//return res
}

//给定整数 n ，返回 所有小于非负整数 n 的质数的数量 。
//示例 1：
//输入：n = 10
//输出：4
//解释：小于 10 的质数一共有 4 个, 它们是 2, 3, 5, 7 。
//示例 2：
//输入：n = 0
//输出：0
//示例 3：
//输入：n = 1
//输出：0
