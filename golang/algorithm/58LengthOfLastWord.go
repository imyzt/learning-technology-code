package main

// 通过空格分割, 计算最后一个字符串的长度
func lengthOfLastWord(s string) int {
	flag, length := false, 0
	for i := len(s) - 1; i >= 0; i-- {
		ch := rune(s[i])
		// 遇到第一个非空格,设置为true
		if !flag && ch != ' ' {
			flag = true
		}
		if flag {
			if ch == ' ' {
				return length
			}
			length++
		}
	}
	return length
}

//Given a string s consisting of words and spaces, return the length of the last word in the string.
//
//A word is a maximal
//substring
// consisting of non-space characters only.
//
//
//
//Example 1:
//
//Input: s = "Hello World"
//Output: 5
//Explanation: The last word is "World" with length 5.
//Example 2:
//
//Input: s = "   fly me   to   the moon  "
//Output: 4
//Explanation: The last word is "moon" with length 4.
//Example 3:
//
//Input: s = "luffy is still joyboy"
//Output: 6
//Explanation: The last word is "joyboy" with length 6.
//
//
//Constraints:
//
//1 <= s.length <= 104
//s consists of only English letters and spaces ' '.
//There will be at least one word in s.
