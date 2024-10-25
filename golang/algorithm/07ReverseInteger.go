package main

import "math"

// 将数字反转，只需模10得到个位，拼接到result的个位上
func reverse07(x int) int {
	absX := x
	if x < 0 {
		absX = -x
	}
	var result int
	for ; absX > 0; absX /= 10 {
		digit := absX % 10
		if result > (math.MaxInt32-digit)/10 {
			return 0
		}
		result = (result * 10) + digit
	}
	if x < 0 {
		result = -result
	}
	return result
}

//Given a signed 32-bit integer x, return x with its digits reversed. If reversing x causes the value to go outside the signed 32-bit integer range [-231, 231 - 1], then return 0.
//
//Assume the environment does not allow you to store 64-bit integers (signed or unsigned).
//
//Example 1:
//
//Input: x = 123
//Output: 321
//Example 2:
//
//Input: x = -123
//Output: -321
//Example 3:
//
//Input: x = 120
//Output: 21
//Constraints:
//
//-231 <= x <= 231 - 1
//Related Topics
//Math
