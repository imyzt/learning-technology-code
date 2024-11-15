package main

import "math"

// 关键点在于, 每进一位(N)都是26的N次方
// 且当前值的数字映射=当前值-64
func titleToNumber(columnTitle string) int {
	res := 0.0
	length := len(columnTitle)
	for i, c := range columnTitle {
		res += float64(c-64) * math.Pow(26.0, float64(length-i-1))
	}
	return int(res)
}

//给定一个 Excel 表格中的列名称，返回其相应的列序号。
//例如，Excel 表格中的列名称 "A" 对应序号 1，"B" 对应序号 2，"C" 对应序号 3，
//以此类推；对于双字符的列名称，如 "AA" 对应序号 27，"AB" 对应序号 28，等等。
