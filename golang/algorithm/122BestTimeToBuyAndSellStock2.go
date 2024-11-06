package main

import "math"

// 计算股票的最佳买入卖出时期(可以买入卖出多次),获得最大利润,动态规划
// 只要明天比今天高,就计算差额
// 此算法有缺陷,即交易次数过多,应该计算到下一次下跌前一天之间最高的一次价格卖出,可以避免多次交易行为
func maxProfit_2_1(prices []int) int {
	maxProfit := 0
	for i := 0; i < len(prices)-1; i++ {
		val := prices[i+1] - prices[i]
		if val > 0 {
			maxProfit += val
		}
	}
	return maxProfit
}
func maxProfit_2_2(prices []int) int {
	profit := 0
	stock := math.MaxInt32
	for _, price := range prices {
		if price < stock {
			stock = price
		}
		if price > stock {
			profit += price - stock
			stock = price
		}
	}
	return profit
}
