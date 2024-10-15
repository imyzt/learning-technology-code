package main

func PalindromeNumber(x int) bool {
	if x < 0 || (x != 0 && x%10 == 0) {
		return false
	}
	var half int
	for half = 0; x > half; x = x / 10 {
		// 将x的最后一位补到half的个位数
		half = (half * 10) + x%10
	}
	return x == half || x == half/10
}
