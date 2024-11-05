package main

// 生成N行的杨辉三角（关键：每一行除首尾元素外，其余元素=上一行（row-1）的相邻元素（column-1、column）的和
// 得到方程：rowData[column] = result[row-1][column-1] + result[row-1][column]
func generate(numRows int) [][]int {
	if numRows == 0 {
		return [][]int{}
	}
	result := make([][]int, numRows)
	for row := 0; row < numRows; row++ {
		rowData := make([]int, row+1)
		rowData[0], rowData[len(rowData)-1] = 1, 1
		for column := 1; column < row; column++ {
			rowData[column] = result[row-1][column-1] + result[row-1][column]
		}
		result[row] = rowData
	}
	return result
}

//题目描述
//给定一个非负整数 numRows，生成「杨辉三角」的前 numRows 行。
//在「杨辉三角」中，每个数是它左上方和右上方的数的和。
//示例
//输入：numRows = 5 输出：
//[
//     [1],
//    [1,1],
//   [1,2,1],
//  [1,3,3,1],
// [1,4,6,4,1]
//]
//说明
//每一行的第一个和最后一个数都是 1。
//每一行的其他数是上一行相邻两个数的和。
//提示
//1 <= numRows <= 30
//思路
//初始化：创建一个二维数组 triangle，用于存储杨辉三角的每一行。
//生成每一行：
//每一行的第一个和最后一个元素都是 1。
//对于其他元素，使用上一行的相邻两个元素之和来计算。
//返回结果：返回生成的二维数组 triangle。
