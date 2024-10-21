package main

// 字符串匹配,第一个出现的位置的索引
func strStr(haystack string, needle string) int {
	needleLen := len(needle)
	if needleLen > len(haystack) {
		return -1
	}
	for i := range haystack {
		if haystack[i:i+needleLen] == needle {
			return i
		}
		if i >= len(haystack)-needleLen {
			return -1
		}
	}
	return -1
}

//Given two strings needle and haystack, return the index of the first
//occurrence of needle in haystack, or -1 if needle is not part of haystack.
//
//
// Example 1:
//
//
//Input: haystack = "sadbutsad", needle = "sad"
//Output: 0
//Explanation: "sad" occurs at index 0 and 6.
//The first occurrence is at index 0, so we return 0.
//
//
// Example 2:
//
//
//Input: haystack = "leetcode", needle = "leeto"
//Output: -1
//Explanation: "leeto" did not occur in "leetcode", so we return -1.
//
//
//
// Constraints:
//
//
// 1 <= haystack.length, needle.length <= 10⁴
// haystack and needle consist of only lowercase English characters.
//
//
// Related Topics Two Pointers String String Matching 👍 6134 👎 449
