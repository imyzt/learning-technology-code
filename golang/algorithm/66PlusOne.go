package main

// 进位运算,
func plusOne(digits []int) []int {
	// 尾插
	plus := 1

	// 简洁代码(code example)
	for i := len(digits) - 1; i >= 0; i-- {
		sum := digits[i] + plus
		// 大于10则进位1,否则进位0
		plus = sum / 10
		// 大于10则当前位取余,否则当前位等于求和的sum
		digits[i] = sum % 10
	}

	// 自己想的方案
	//for i := len(digits) - 1; i >= 0; i-- {
	//	sum := digits[i] + plus
	//	if sum > 9 {
	//		digits[i] = sum % 10
	//	} else {
	//		digits[i] = sum
	//		plus = 0
	//	}
	//}

	// 最前面的进位
	if plus != 0 {
		digits = append([]int{plus}, digits...)
	}
	return digits
}

//You are given a large integer represented as an integer array digits, where each digits[i] is the ith digit of the integer. The digits are ordered from most significant to least significant in left-to-right order. The large integer does not contain any leading 0's.
//
//Increment the large integer by one and return the resulting array of digits.
//
//
//
//Example 1:
//
//Input: digits = [1,2,3]
//Output: [1,2,4]
//Explanation: The array represents the integer 123.
//Incrementing by one gives 123 + 1 = 124.
//Thus, the result should be [1,2,4].
//Example 2:
//
//Input: digits = [4,3,2,1]
//Output: [4,3,2,2]
//Explanation: The array represents the integer 4321.
//Incrementing by one gives 4321 + 1 = 4322.
//Thus, the result should be [4,3,2,2].
//Example 3:
//
//Input: digits = [9]
//Output: [1,0]
//Explanation: The array represents the integer 9.
//Incrementing by one gives 9 + 1 = 10.
//Thus, the result should be [1,0].
//
//
//Constraints:
//
//1 <= digits.length <= 100
//0 <= digits[i] <= 9
//digits does not contain any leading 0's.
