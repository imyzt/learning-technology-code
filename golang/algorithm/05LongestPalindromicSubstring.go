package main

import "fmt"

func longestPalindrome(s string) string {
	n := len(s)
	if n == 0 {
		return ""
	}
	dp := make([][]bool, n)
	for i := range dp {
		dp[i] = make([]bool, n)
		dp[i][i] = true
	}
	start, mLen := 0, 1
	for length := 2; length <= n; length++ {
		for i := 0; i <= n-length; i++ {
			j := i + length - 1
			if s[i] == s[j] && (length == 2 || dp[i+1][j-1]) {
				fmt.Printf("length=%d,s[%d]=%s,s[%d]=%s, dp[%d][%d]=%v\n", length, i, string(s[i]), j, string(s[j]), i+1, j-1, dp[i+1][j-1])
				dp[i][j] = true
				if length > mLen {
					start = i
					mLen = length
				}
			}
		}
	}
	return s[start : start+mLen]
}

//Given a string s, return the longest palindromic substring in s.
//
//Example 1:
//
//Input: s = "babad"
//Output: "bab"
//Explanation: "aba" is also a valid answer.
//Example 2:
//
//Input: s = "cbbd"
//Output: "bb"
//Constraints:
//
//1 <= s.length <= 1000
//s consist of only digits and English letters.
//Related Topics
//Two Pointers
//String
//Dynamic Programming
