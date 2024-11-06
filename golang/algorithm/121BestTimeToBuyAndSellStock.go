package main

// 计算股票的最佳买入卖出时期(只能买入卖出一次),获得最大利润,动态规划
// 不断计算当前是否最低价格,将最低价格存起来,和每次价格比较获得利润最大值
func maxProfit(prices []int) int {
	maxProfit, minPrice := 0, prices[0]
	for i := 1; i < len(prices); i++ {
		// 当前价格是否是最低价格
		minPrice = min(minPrice, prices[i])
		// 利润=当前价格-最低的价格
		profit := prices[i] - minPrice
		// 当前利润是否是最大利润
		maxProfit = max(profit, maxProfit)
	}
	return maxProfit
}
