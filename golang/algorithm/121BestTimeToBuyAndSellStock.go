package main

func maxProfit(prices []int) int {

	//dp := make([]int, len(prices))
	maxProfit := 0
	for i := 0; i < len(prices)-1; i++ {

		for j := i + 1; j < len(prices)-1; j++ {
			val := prices[j] - prices[i]
			if val > 0 && maxProfit < val {
				maxProfit = val
			}
		}
		//if prices[i+1]-prices[i] > 0 {
		//
		//	maxProfit = prices[i+1] - prices[i]
		//}

	}
	return maxProfit
}
