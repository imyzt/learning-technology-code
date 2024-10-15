package main

func longestCommonPrefix(strs []string) string {
	shortStr := strs[0]
	for _, str := range strs[1:] {
		strLen := len(shortStr)
		if strLen == 0 {
			return ""
		}
		if len(str) < strLen {
			shortStr = str
		}
	}
	var prefix string
	for j := 0; j < len(shortStr); j++ {
		flag := true
		for i := 0; i < len(strs); i++ {
			if string(strs[i][j]) != string(shortStr[j]) {
				flag = false
			}
		}
		if flag {
			prefix += string(shortStr[j])
		} else {
			break
		}
	}

	//  if len(strs) == 0 {
	//        return ""
	//    }
	//    temp := strs[0]
	//    for i:=1;i<len(strs);i++{
	//        for strings.Index(strs[i], temp) != 0 {
	//            temp = temp[:len(temp)-1]
	//            if temp == "" {
	//                return ""
	//            }
	//        }
	//    }
	return prefix
}

//Write a function to find the longest common prefix string amongst an array of strings.
//
//If there is no common prefix, return an empty string "".
//
//
//
//Example 1:
//
//Input: strs = ["flower","flow","flight"]
//Output: "fl"
//Example 2:
//
//Input: strs = ["dog","racecar","car"]
//Output: ""
//Explanation: There is no common prefix among the input strings.
//
//
//Constraints:
//
//1 <= strs.length <= 200
//0 <= strs[i].length <= 200
//strs[i] consists of only lowercase English letters.
