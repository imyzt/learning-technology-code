package main

func minimumTotal(triangle [][]int) int {

	// 只需要存储上一行
	dp := make([]int, len(triangle))
	dp[0] = triangle[0][0]

	for i := 1; i < len(triangle); i++ {
		// 从后往前遍历
		for j := i; j >= 0; j-- {
			if j == i {
				// 最后一个元素，上一行当前索引-1 + 当前元素
				dp[j] = dp[j-1] + triangle[i][j]
			} else if j == 0 {
				// 第一个元素，上一行当前索引 + 当前元素
				dp[j] = dp[j] + triangle[i][j]
			} else {
				// 中间元素，min(上一行当前索引-1, 上一行当前索引) + 当前元素
				dp[j] = min(dp[j-1], dp[j]) + triangle[i][j]
			}
		}
	}

	// 找到每一个路径中的最小值
	minPathSum := dp[0]
	for _, val := range dp {
		if val < minPathSum {
			minPathSum = val
		}
	}

	return minPathSum
}
