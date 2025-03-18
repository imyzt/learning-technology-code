package top.imyzt.learning.algorithm.substr;


/**
 * @author imyzt
 * @date 2025/03/18
 * @description 双指针查找子串
 */
public class SubString {

    public static void main(String[] args) {
        String b = "abe";
        System.out.println(findSub("abc", b));
        System.out.println(findSub("abe", b));
        System.out.println(findSub("aaaaabe", b));
        System.out.println(findSub("ddd", b));
        System.out.println(findSub("a", b));
    }

    private static int findSub(String a, String b) {
        int n = a.length(), m = b.length();
        if (n == 0 || m == 0 || m > n) {
            return -1;
        }
        int i = 0, j = 0, k = 0;
        while (i < a.length() && j < b.length()) {
            if (a.charAt(i) == b.charAt(j)) {
                i++;
                j++;
            } else {
                k++;
                i = k;
                j = 0;
            }
        }
        if (m == j) {
            return k;
        } else {
            return -1;
        }
    }
}
