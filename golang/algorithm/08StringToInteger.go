package main

func myAtoi(s string) int {
	negative, number := false, 0
	first := true
	for _, c := range s {
		char := string(c)
		if first && char == " " {
			// 去除前导空格
			continue
		}
		if char == "-" {
			// 处理符号
			if first {
				negative = true
				continue
			} else {
				break
			}
		}
		first = false
		digit := int(c - '0')
		if digit >= 0 && digit < 10 {
			number = (number * 10) + digit
		} else {
			break
		}
	}
	if negative {
		return -number
	}
	return number
}
