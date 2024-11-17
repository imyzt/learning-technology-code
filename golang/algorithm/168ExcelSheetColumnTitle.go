package main

func convertToTitle(columnNumber int) string {
	res := ""
	for columnNumber > 0 {
		num := columnNumber % 26
		if num == 0 {
			// 恰好=26时,需要将值减去26
			num = 26
			columnNumber -= 26
		}
		res = string(num+64) + res
		columnNumber /= 26
	}
	return res
}
