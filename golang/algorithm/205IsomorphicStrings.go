package main

func isIsomorphic(s string, t string) bool {
	if len(s) != len(t) {
		return false
	}
	mapS := make(map[byte]byte, len(s))
	mapT := make(map[byte]byte, len(t))
	for i := 0; i < len(s); i++ {
		charS, charT := s[i], t[i]
		if v, ok := mapS[charS]; ok {
			if v != charT {
				return false
			}
		} else {
			mapS[charS] = charT
		}
		if v, ok := mapT[charT]; ok {
			if v != charS {
				return false
			}
		} else {
			mapT[charT] = charS
		}
	}
	return true
}

//给定两个字符串 s 和 t ，判断它们是否是同构的。
//如果 s 中的字符可以按某种映射关系替换得到 t ，那么这两个字符串是同构的。
//每个出现的字符都应当映射到另一个字符，同时不改变字符的顺序。不同字符不能映射到同一个字符上，相同字符只能映射到同一个字符上，字符可以映射到自己本身。
//Example 1:
//Input: s = "egg", t = "add"
//Output: true
//Explanation:
//The strings s and t can be made identical by:
//Mapping 'e' to 'a'.
//Mapping 'g' to 'd'.
//Example 2:
//Input: s = "foo", t = "bar"
//Output: false
//Explanation:
//The strings s and t can not be made identical as 'o' needs to be mapped to both 'a' and 'r'.
//Example 3:
//Input: s = "paper", t = "title"
//Output: true
