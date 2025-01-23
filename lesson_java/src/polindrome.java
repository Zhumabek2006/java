import java.util.Scanner;

public class polindrome {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Prompt user for input
        System.out.println("Enter a string: ");
        String s = sc.next();


        //s = s.replaceAll("\\s+", "").toLowerCase();
        boolean result = isPalindrome(s, 0, s.length() - 1);
        if (result) {
            System.out.println("The string is a palindrome.");
        } else {
            System.out.println("The string is not a palindrome.");
        }

        sc.close();
    }

    public static boolean isPalindrome(String s, int start, int end) {
        if (start >= end) {
            return true;
        }

        if (s.charAt(start) == s.charAt(end)) {
            return isPalindrome(s, start + 1, end - 1);
        } else {
            return false;
        }
    }
}
