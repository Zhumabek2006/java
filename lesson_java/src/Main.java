import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter the size of the array:");
        int n = scan.nextInt();

        // Initialize the array
        int[] arr = new int[n];
        System.out.println("Enter " + n + " elements:");
        for (int i = 0; i < n; i++) {
            arr[i] = scan.nextInt();
        }

        int sum = 0;
        int min = Integer.MAX_VALUE;
        int secondMin = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int secondMax = Integer.MIN_VALUE;

        // Loop through the array
        for (int i = 0; i < n; i++) {
            int value = arr[i];
            sum += value;

            if (value < min) {
                secondMin = min;
                min = value;
            } else if (value < secondMin && value != min) {
                secondMin = value;
            }

            if (value > max) {
                secondMax = max;
                max = value;
            } else if (value > secondMax && value != max) {
                secondMax = value;
            }
        }

        double avg = sum / n;
        System.out.println("The sum is: " + sum);
        System.out.println("The average is: " + avg);
        System.out.println("The minimum is: " + min);
        System.out.println("The second minimum is: " + secondMin);
        System.out.println("The maximum is: " + max);
        System.out.println("The second maximum is: " + secondMax);

        scan.close();
    }
}
