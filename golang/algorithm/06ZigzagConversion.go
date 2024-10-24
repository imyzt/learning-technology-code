package main

import "fmt"

func convert(s string, numRows int) string {

	// 锯齿状，等比数列
	// 14/3=4
	// s[0]，      s[4]，    s[8]，      s[12]   4
	// s[1]，s[3]，s[5]，s[7],s[9],s[11] s[13]      2
	// s[2],      s[6],     s[10],           4
	// 14/4=3
	// s[0]           s[6]              s[12]   6
	// s[1]      s[5] s[7]        s[11] s[13]   4,2,4,2
	// s[2] s[4]      s[8]  s[10]               2,4,2
	// s[3]           s[9]                      6

	sLen := len(s)
	// 列数量, numRows=行数量
	numColumns := sLen / numRows

	for r := 0; r < numRows; r++ {
		for c := 0; c < numColumns; c++ {
			i := (c * numColumns) + r
			if r%numRows != 0 && c%numColumns != 0 {
				// 当前行打印锯齿,
				//当前列打印锯齿
				fmt.Printf("%s ", string(s[c+numColumns]))
			} else {

				// 不用打印锯齿
			}
			fmt.Printf("%s ", string(s[i]))
			fmt.Printf(" ")
		}
		fmt.Println()
	}
	return ""
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
