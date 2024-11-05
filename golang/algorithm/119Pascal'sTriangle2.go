package main

// 返回第N行的杨辉三角（关键：每一行除首尾元素外，其余元素=上一行（row-1）的相邻元素（column-1、column）的和
// 得到方程：rowData[column] = result[row-1][column-1] + result[row-1][column]

func getRow(rowIndex int) []int {
	result := make([]int, 1)
	for i := 0; i <= rowIndex; i++ {
		rowData := make([]int, i+1)
		rowData[0], rowData[len(rowData)-1] = 1, 1
		for column := 1; column < i; column++ {
			rowData[column] = result[column-1] + result[column]
		}
		result = rowData
	}
	return result
}

// 另一种直接得到杨辉三角第N行的方程，使用递推公式
// res[column] = res[column-1] * (rowIndex - column + 1) / column
func getRow2(rowIndex int) []int {
	length := rowIndex + 1
	res := make([]int, length)
	res[0] = 1
	for column := 1; column < length; column++ {
		res[column] = res[column-1] * (rowIndex - column + 1) / column
	}
	return res
}
