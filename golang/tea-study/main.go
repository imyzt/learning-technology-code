package main

func main() {
	print(isPalindrome(121))
}

// 判断回文数
func isPalindrome(x int) bool {
	// 负数,10的倍数可以直接排除
	if x < 0 || (x != 0 && x%10 == 0) {
		return false
	}
	var half int
	// 只要比x大,就不断将x除10(方便拿到x的最后一位)
	for half = 0; x > half; x = x / 10 {
		// half*10: 用于将half上一轮计算结果的最后一位往前移一位
		// +x%10: 用于将x的最后一位作为half的最后一位
		half = (half * 10) + x%10
	}
	// 相等(1221->1221)-偶数回文数
	// 或者(121->121)-奇数回文数, 因为奇数回文数的for循环half=1
	// half=(0*10)+1=1, x=12
	// half=(1*10)+2=12, x=1
	// x!>half, 退出for,此时就需要将half/10来和x对比是否相等
	return x == half || x == half/10
}
