package expression;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int x = Integer.MIN_VALUE;
        while (x == Integer.MIN_VALUE) {
            System.out.println("Please enter the value of variable X: ");
            try {
                x = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Input error.");
            }
        }

        scanner.close();

        AnyExpression e = new Subtract(
                new Multiply(
                        new Const(2),
                        new Variable("x")
                ),
                new Const(3)
        );

        System.out.println(e.evaluate(x, 0, 0));
    }
}
