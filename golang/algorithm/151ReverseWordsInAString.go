package main

import "strings"

// 翻转字符串，需要考虑的是各种多余的空格问题
// 借助内置函数完成字符串的拆分，然后利用循环翻转数组元素
func reverseWords(s string) string {
	s = strings.TrimSpace(s)
	words := strings.Fields(s)
	for i, j := 0, len(words)-1; i < j; i, j = i+1, j-1 {
		words[i], words[j] = words[j], words[i]
	}
	return strings.Join(words, " ")
}
