package main

// 计算股票的最佳买入卖出时期(最多只能买入卖出2次),获得最大利润,动态规划

func maxProfit_3(prices []int) int {
	if len(prices) == 0 {
		return 0
	}

	n := len(prices)
	k := 2
	dp := make([][][]int, n)
	for i := range dp {
		dp[i] = make([][]int, k+1)
		for j := range dp[i] {
			dp[i][j] = make([]int, 2)
		}
	}

	// 初始化
	for i := 0; i < n; i++ {
		// 第 i 天不进行任何交易且不持有股票的利润为 0
		dp[i][0][0] = 0
		// 第 i 天不进行任何交易但持有股票的利润为 -prices[i]（因为只能在当天买入）
		dp[i][0][1] = -prices[i]
	}

	for j := 1; j <= k; j++ {
		// 第 0 天进行 j 次交易且不持有股票的利润为 0
		dp[0][j][0] = 0
		// 第 0 天进行 j 次交易且持有股票的利润为 -prices[0]（因为只能在当天买入）
		dp[0][j][1] = -prices[0]
	}

	// 状态转移
	for i := 1; i < n; i++ {
		for j := 1; j <= k; j++ {
			// 第 i 天不持有股票的最大利润
			// 要么是前一天也不持有股票，要么是前一天持有股票并在第 i 天卖出
			dp[i][j][0] = max(dp[i-1][j][0], dp[i-1][j][1]+prices[i])

			// 第 i 天持有股票的最大利润
			// 要么是前一天也持有股票，要么是前一天不持有股票并在第 i 天买入
			dp[i][j][1] = max(dp[i-1][j][1], dp[i-1][j-1][0]-prices[i])
		}
	}

	// 返回第 n-1 天结束时，最多进行 2 笔交易且不持有股票的最大利润
	return dp[n-1][k][0]
}
