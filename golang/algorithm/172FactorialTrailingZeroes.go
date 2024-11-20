package main

// 计算数字n的阶乘末尾有多少个0
// 在计算阶乘结果末尾零的数量时，我们需要理解末尾零的来源。末尾零是由因子 10 产生的，而 10 可以分解为 2 和 5。
// 在阶乘中，2 的数量总是多于 5 的数量，因此问题转化为计算阶乘中 5 的因子数量。
func trailingZeroes(n int) int {
	count := 0
	for n > 0 {
		n /= 5
		count += n
	}
	return count

	// 暴力破解
	//res := 1
	//for n > 0 {
	//	res *= n
	//	n--
	//}
	//fmt.Println(res)
	//r := 0
	//for res > 0 {
	//	if res%10 == 0 {
	//		r++
	//		res /= 10
	//	} else {
	//		return r
	//	}
	//}
	//return r
}
