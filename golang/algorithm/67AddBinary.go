package main

import "strconv"

func addBinary(a string, b string) string {

	// 要点，记录是否需要累加
	carry, wa, wb, result := 0, len(a)-1, len(b)-1, ""
	for wa >= 0 || wb >= 0 || carry > 0 {
		sum := carry
		if wa >= 0 {
			// ASCII, 1=49，0=48, 减'0'是为了转换得到0和1
			sum += int(a[wa] - '0')
			wa--
		}
		if wb >= 0 {
			sum += int(b[wb] - '0')
			wb--
		}
		// sum=[0,1,2,3],此处carry可能=[0,1],carry决定下一位是否需要+1
		carry = sum / 2
		result = strconv.Itoa(sum%2) + result
	}
	return result
}
