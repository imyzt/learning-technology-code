package main

import (
	"unicode"
)

// 验证是否为回文数，双指针
func isPalindrome(s string) bool {

	left, right := 0, len(s)-1

	for right > left {
		// switch 3ms内
		switch {
		case !isValid2(s[left]):
			left++
		case !isValid2(s[right]):
			right--
		case equalsIgnore1(s[left], s[right]):
			left++
			right--
		default:
			return false
		}
		// if语句至少需要50ms
		//if !isValid2(s[left]) {
		//	left++
		//	continue
		//}
		//if !isValid2(s[right]) {
		//	right--
		//	continue
		//}
		//if equalsIgnore2(s[left], s[right]) {
		//	fmt.Printf("%v \t %v \n", string(s[left]), string(s[right]))
		//	left++
		//	right--
		//} else {
		//	return false
		//}
	}
	return true
}

func equalsIgnore2(b1 byte, b2 byte) bool {
	return unicode.ToLower(rune(b1)) == unicode.ToLower(rune(b2))
}
func isValid2(b byte) bool {
	return unicode.IsLetter(rune(b)) || unicode.IsDigit(rune(b))
}

func toLower(b byte) byte {
	if b >= 65 && b <= 90 {
		return b + 32
	}
	return b
}
func equalsIgnore1(b1 byte, b2 byte) bool {
	return toLower(b1) == toLower(b2)
}
func isValid1(b byte) bool {
	if b >= 65 && b <= 90 {
		return true
	}
	if b >= 97 && b <= 122 {
		return true
	}
	if b >= 48 && b <= 57 {
		return true
	}
	return false
}
