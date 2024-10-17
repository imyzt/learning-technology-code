package main

func longestCommonPrefix(strs []string) string {
	//方案1，先找到最短字符串
	//shortStr := strs[0]
	////先找到最短字符串
	//for _, str := range strs[1:] {
	//	strLen := len(shortStr)
	//	if strLen == 0 {
	//		return ""
	//	}
	//	if len(str) < strLen {
	//		shortStr = str
	//	}
	//}
	//var prefix string
	////循环最短字符串的每个字符
	//for j := 0; j < len(shortStr); j++ {
	//	flag := true
	//	//对比每个字符串
	//	for i := 0; i < len(strs); i++ {
	//		//如果有任何一位的字符串和最短字符串不相等，就跳出
	//		if string(strs[i][j]) != string(shortStr[j]) {
	//			flag = false
	//		}
	//	}
	//	if flag {
	//		prefix += string(shortStr[j])
	//	} else {
	//		break
	//	}
	//}

	//方案2，用第一个字符串作为循环依据
	var prefix string
	firstStr := strs[0]
label:
	for j := 0; j < len(firstStr); j++ {
		flag := true
		for i := 1; i < len(strs); i++ {
			v := strs[i]
			//循环次数超过当前字符串的长度，直接跳出
			if j >= len(v) {
				break label
			}
			//如果有任何一位的字符串和最短字符串不相等，就跳出
			if string(v[j]) != string(firstStr[j]) {
				flag = false
			}
		}
		if flag {
			prefix += string(firstStr[j])
		} else {
			break
		}
	}

	//方案3，借助查找函数
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
