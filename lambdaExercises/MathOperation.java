package lambdaExercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@FunctionalInterface
interface MathOperation {
    int Operation(int a, int b);
}

class Main {

    public static void main(String[] args) {
        MathOperation Addition = (a, b) -> a + b;
        MathOperation Subtraction = (a, b) -> a - b;
        MathOperation Multiplication = (a, b) -> a * b;
        MathOperation Division = (a, b) -> a / b;

        System.out.println("Addition: " + Addition.Operation(8, 2));
        System.out.println("Subtraction: " + Subtraction.Operation(8, 2));
        System.out.println("Multiplication: " + Multiplication.Operation(8, 2));
        System.out.println("Division: " + Division.Operation(8, 2));

        System.out.printf("__________________________________________________________");

        List<Integer> numbers = Arrays.asList(10, 15, 22, 33, 40, 55);
        List<Integer> oddNumbers = new ArrayList<>();

        Predicate<Integer> Odd = num -> num % 2 != 0;

        for (Integer num : numbers) {
            if (Odd.test(num)) {
                oddNumbers.add(num);
            }
        }
        System.out.println("Odd Numbers: " + oddNumbers);

        System.out.printf("__________________________________________________________");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        names.sort((a, b) -> b.compareTo(a));

        System.out.println("Sorted Names: " + names);

        List<String> words = Arrays.asList("hello", "java", "lambda");

        Function<String, String> transform = str ->
                new StringBuilder(str.toUpperCase()).reverse().toString();

        List<String> transformedWords = words.stream()
                .map(transform)
                .collect(Collectors.toList());

        System.out.println("Transformed Strings: " + transformedWords);

        System.out.printf("__________________________________________________________");


        List<String> cities1 = Arrays.asList("New York", "London", "Tokyo", "Berlin");

        Consumer<String> printCity = city -> System.out.println(city);

        cities1.forEach(printCity);

        System.out.printf("__________________________________________________________");

        List<String> cities2 = Arrays.asList("New York", "London", "Tokyo", "Berlin");

        cities2.forEach(System.out::println);

        System.out.printf("__________________________________________________________");

        BiFunction<Integer, Integer, Integer> maxFunction = (x, y) -> Math.max(x, y);
        BiFunction<Integer, Integer, Integer> minFunction = (x, y) -> Math.min(x, y);

        System.out.println("Max: " + maxFunction.apply(13, 14));
        System.out.println("Min: " + minFunction.apply(8, 16));

    }
}
