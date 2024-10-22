package main

// 滑动窗口, 确保窗口内所有元素都是唯一的
func lengthOfLongestSubstring(s string) int {
	if len(s) <= 1 {
		return len(s)
	}
	left, mLen, m := 0, 0, make(map[byte]int)
	for right := range s {
		// 当右窗口移动到一个已经存在的元素时,左窗口直接移动到已存在元素上次出现的索引位置+1
		// idx >= left 的作用是确保窗口内不存在重复元素
		if idx, ok := m[s[right]]; ok && idx >= left {
			left = idx + 1
		}
		if right-left+1 > mLen {
			// 如果当前窗口的长度大于之前其他窗口, 则设置最大长度等于当前窗口
			mLen = right - left + 1
		}
		// 将当前元素添加到去重map中
		m[s[right]] = right
	}
	return mLen
}

//Given a string s, find the length of the longest
//substring
// without repeating characters.
//
//
//
//Example 1:
//
//Input: s = "abcabcbb"
//Output: 3
//Explanation: The answer is "abc", with the length of 3.
//Example 2:
//
//Input: s = "bbbbb"
//Output: 1
//Explanation: The answer is "b", with the length of 1.
//Example 3:
//
//Input: s = "pwwkew"
//Output: 3
//Explanation: The answer is "wke", with the length of 3.
//Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
//
//
//Constraints:
//
//0 <= s.length <= 5 * 104
//s consists of English letters, digits, symbols and spaces.
