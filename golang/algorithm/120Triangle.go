package main

func minimumTotal(triangle [][]int) int {

	dp := make([][]int, len(triangle), len(triangle))
	for i := 0; i < len(triangle); i++ {
		dp[i] = make([]int, i+1)
	}

	for i := 0; i < len(triangle); i++ {
		for j, column := range triangle[i] {
			dp[i][j] = min(triangle[i+1][j+1], triangle[i+1][j]) + column
		}
	}
	return 1
}
