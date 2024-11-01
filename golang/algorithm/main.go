package main

import "fmt"

func main() {
	//fmt.Println(PalindromeNumber(0))
	//fmt.Println(PalindromeNumber(121))
	//fmt.Println(PalindromeNumber(110))

	//println(romanToInt("IM"))
	//println(romanToInt("III"))
	//println(romanToInt("LVIII"))
	//println(romanToInt("MCMXCIV"))

	//fmt.Println(twoSum2([]int{2, 5, 5, 11}, 10))

	//fmt.Println(longestCommonPrefix([]string{"flower", "flow", "flight"}))
	//fmt.Println(longestCommonPrefix([]string{"ab", "a"}))
	//fmt.Println(longestCommonPrefix([]string{"cir", "car"}))
	//fmt.Println(longestCommonPrefix([]string{"caa", "", "a", "acb"}))

	//fmt.Println(isValid("("))
	//fmt.Println(isValid("{}"))

	//l1 := &ListNode{Val: 1, Next: &ListNode{Val: 2, Next: &ListNode{Val: 4}}}
	//l2 := &ListNode{Val: 1, Next: &ListNode{Val: 3, Next: &ListNode{Val: 4}}}
	//fmt.Printf("%v", mergeTwoLists(l1, l2))

	//fmt.Println(removeDuplicates([]int{0, 0, 1, 1, 1, 2, 2, 3, 3, 4}))

	//fmt.Println(removeElement([]int{3, 2, 2, 3}, 3))

	//fmt.Println(strStr("sa2dbuts3ad", "sad"))

	//fmt.Println(searchInsert([]int{1, 3, 5, 6}, 2))
	//fmt.Println(searchInsert([]int{1, 3, 5, 6}, 7))
	//fmt.Println(searchInsert([]int{1, 3, 5, 6}, 0))
	//fmt.Println(searchInsert([]int{1, 13, 25, 36, 50}, 34))

	//fmt.Println(lengthOfLastWord("Hello World"))
	//fmt.Println(lengthOfLastWord("   fly me   to   the moon  "))
	//fmt.Println(lengthOfLastWord("luffy is still joyboy"))
	//fmt.Println(lengthOfLastWord("a"))

	//fmt.Println(plusOne([]int{9, 9}))
	//fmt.Println(plusOne([]int{4, 3, 2, 1}))
	//fmt.Println(plusOne([]int{1, 2, 3}))

	//fmt.Println(reverseList(&ListNode{Val: 0, Next: &ListNode{Val: 1, Next: &ListNode{Val: 2, Next: &ListNode{Val: 3, Next: nil}}}}))

	//fmt.Println(compress([]byte{'a', 'a', 'b', 'b', 'c', 'c', 'c'}))
	//fmt.Println(compress([]byte{'a'}))
	//fmt.Println(compress([]byte{'a', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b'}))

	//fmt.Println(addBinary("11", "1"))
	//fmt.Println(addBinary("1010", "1011"))

	//l1 = [2,4,3], l2 = [5,6,4]
	//l1 := &ListNode{Val: 2, Next: &ListNode{Val: 4, Next: &ListNode{Val: 3}}}
	//l2 := &ListNode{Val: 5, Next: &ListNode{Val: 6, Next: &ListNode{Val: 4}}}
	//fmt.Println(addTwoNumbers(l1, l2))

	//fmt.Println(lengthOfLongestSubstring("abcabcbb"))
	//fmt.Println(lengthOfLongestSubstring("bbbbb"))
	//fmt.Println(lengthOfLongestSubstring("pwwkew"))
	//fmt.Println(lengthOfLongestSubstring("a"))
	//fmt.Println(lengthOfLongestSubstring(" "))
	//fmt.Println(lengthOfLongestSubstring("au"))
	//fmt.Println(lengthOfLongestSubstring("aab"))
	//fmt.Println(lengthOfLongestSubstring("abba"))

	//fmt.Println(longestPalindrome("babad"))
	//fmt.Println(longestPalindrome("cbbd”"))

	//fmt.Println(convert("PAYPALISHIRING", 3))
	//fmt.Println(convert("AB", 1))

	//fmt.Println(reverse07(123))
	//fmt.Println(reverse07(-123))
	//fmt.Println(reverse07(120))
	//fmt.Println(reverse07(0))
	//fmt.Println(reverse07(1534236469))

	//fmt.Println(myAtoi("0-1"))
	//fmt.Println(myAtoi("1337c0d3"))
	//fmt.Println(myAtoi("42"))
	//fmt.Println(myAtoi("-042"))
	//fmt.Println(myAtoi("words and 987"))
	//fmt.Println(myAtoi("-91283472332"))

	//fmt.Println(maxArea([]int{1, 8, 6, 2, 5, 4, 8, 3, 7}))
	//fmt.Println(maxArea([]int{1, 1}))
	//fmt.Println(maxArea([]int{2, 3, 4, 5, 18, 17, 6}))

	//fmt.Println(threeSum([]int{-1, 0, 1, 2, -1, -4}))

	//fmt.Println(mySqrt(4))
	//fmt.Println(mySqrt(8))
	//fmt.Println(mySqrt(1))

	//fmt.Println(climbStairs(10))
	//fmt.Println(climbStairs(3))
	//fmt.Println(climbStairs(2))

	//fmt.Println(deleteDuplicates(&ListNode{Val: 1, Next: &ListNode{Val: 1, Next: &ListNode{Val: 2, Next: &ListNode{Val: 3, Next: &ListNode{Val: 3}}}}}))
	//fmt.Println(deleteDuplicates(&ListNode{Val: 1, Next: &ListNode{Val: 1, Next: &ListNode{Val: 2}}}))

	//merge([]int{1, 2, 3, 0, 0, 0}, 3, []int{2, 5, 6}, 3)
	//merge([]int{0}, 0, []int{1}, 1)

	root := &TreeNode{
		Val: 5,
		Left: &TreeNode{
			Val: 3,
			Left: &TreeNode{
				Val: 2,
				Left: &TreeNode{
					Val: 1,
				},
				Right: nil,
			},
			Right: &TreeNode{
				Val: 4,
			},
		},
		Right: &TreeNode{
			Val: 6,
		},
	}
	fmt.Println(inorderTraversal(root))
}
