package main

import "strings"

func reverseWords(s string) string {
	s = strings.Trim(s, " ")
	word := ""
	res := ""
	for i := range s {
		word += string(s[i])
		if s[i] != ' ' {
			if i != len(s)-1 {
				continue
			}
		}
		if word == " " {
			word = ""
			continue
		}
		res = word + " " + res
		word = ""
	}
	return res[:len(s)]
}
