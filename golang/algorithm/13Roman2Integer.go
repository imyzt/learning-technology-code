package main

// 罗马数字转普通数字, 单纯判断当前位是否小于下一位, 如果小于则先减掉, 下一位会加上当前数的整数
func romanToInt(s string) int {
	romanMap := map[string]int{"I": 1, "V": 5, "X": 10, "L": 50, "C": 100, "D": 500, "M": 1000}

	if len(s) == 1 {
		return romanMap[s]
	}

	var result int
	for i := 0; i < len(s)-1; i++ {
		curr := romanMap[string(s[i])]
		last := romanMap[string(s[i+1])]
		if curr < last {
			//先减小数，下一个循环时，会加上这一对数对应的整数
			result -= curr
		} else {
			result += curr
		}
	}
	//需要加上最后一位(个位)
	result = result + romanMap[string(s[len(s)-1])]

	//result := romanMap[string(s[len(s)-1])]
	//for i := len(s) - 2; i >= 0; i-- {
	//	curr := romanMap[string(s[i])]
	//	last := romanMap[string(s[i+1])]
	//	if curr >= last {
	//		result += curr
	//	} else {
	//		result -= curr
	//	}
	//}

	return result
}

//res := romanMap[string(s[len(s)-1])]
//for i := len(s) - 2; i >= 0; i-- {
//	if romanMap[string(s[i])] >= romanMap[string(s[i+1])] {
//		res += romanMap[string(s[i])]
//	} else {
//		res -= romanMap[string(s[i])]
//	}
//}
//Roman numerals are represented by seven different symbols: I, V, X, L, C, D and M.
//
//Symbol       Value
//I             1
//V             5
//X             10
//L             50
//C             100
//D             500
//M             1000
//For example, 2 is written as II in Roman numeral, just two ones added together. 12 is written as XII, which is simply X + II. The number 27 is written as XXVII, which is XX + V + II.
//
//Roman numerals are usually written largest to smallest from left to right. However, the numeral for four is not IIII. Instead, the number four is written as IV. Because the one is before the five we subtract it making four. The same principle applies to the number nine, which is written as IX. There are six instances where subtraction is used:
//
//I can be placed before V (5) and X (10) to make 4 and 9.
//X can be placed before L (50) and C (100) to make 40 and 90.
//C can be placed before D (500) and M (1000) to make 400 and 900.
//Given a roman numeral, convert it to an integer.
//
//
//
//Example 1:
//
//Input: s = "III"
//Output: 3
//Explanation: III = 3.
//Example 2:
//
//Input: s = "LVIII"
//Output: 58
//Explanation: L = 50, V= 5, III = 3.
//Example 3:
//
//Input: s = "MCMXCIV"
//Output: 1994
//Explanation: M = 1000, CM = 900, XC = 90 and IV = 4.
//
//
//Constraints:
//
//1 <= s.length <= 15
//s contains only the characters ('I', 'V', 'X', 'L', 'C', 'D', 'M').
//It is guaranteed that s is a valid roman numeral in the range [1, 3999].
