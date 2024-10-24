package main

// Z字形变换
// 第一行和最后一行时调换方向是关键, 开始向下,然后向上
// 第0行  1↓    5↓     9↓
// 第1行  2↓ 4↑ 6↓ 8↑ 10↓
// 第2行  3↓    7↓    11↓
func convert(s string, numRows int) string {
	if numRows == 1 {
		return s
	}
	// rows用来存储每一行的子串
	// row代表行号, 上下上的不断换行
	// down代表方向, 向下-true,向上-false
	rows, row, down := make([]string, numRows), 0, false
	for i := range s {
		rows[row] += string(s[i])
		if row == 0 || row == numRows-1 {
			// 调换方向
			down = !down
		}
		if down {
			row++
		} else {
			row--
		}
	}
	ans := ""
	for i := range rows {
		ans += rows[i]
	}
	return ans
}

//The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this: (you may want to display this pattern in a fixed font for better legibility)
//
//P   A   H   N
//A P L S I I G
//Y   I   R
//And then read line by line: "PAHNAPLSIIGYIR"
//
//Write the code that will take a string and make this conversion given a number of rows:
//
//string convert(string s, int numRows);
//Example 1:
//
//Input: s = "PAYPALISHIRING", numRows = 3
//Output: "PAHNAPLSIIGYIR"
//Example 2:
//
//Input: s = "PAYPALISHIRING", numRows = 4
//Output: "PINALSIGYAHRPI"
//Explanation:
//P     I    N
//A   L S  I G
//Y A   H R
//P     I
//Example 3:
//
//Input: s = "A", numRows = 1
//Output: "A"
//Constraints:
//
//1 <= s.length <= 1000
//s consists of English letters (lower-case and upper-case), ',' and '.'.
//1 <= numRows <= 1000
//Related Topics
//String
