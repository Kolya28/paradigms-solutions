package expression.exceptions;

import expression.TripleExpression;

public class Main {
    public static void main(String[] args) {
        TripleExpression expression;
        try {
            expression = new ExpressionParser().parse("1000000*x*x*x*x*x/(x-1)");
        } catch (ExpressionParseException e) {
            System.err.println("ParseException :" + e.getLocalizedMessage());
            return;
        } catch (Exception e) {
            System.err.println("Unknown Exception :" + e.getLocalizedMessage());
            return;
        }

        System.out.println("x\tf");
        int overflowsInRow = 0;

        for (int x = 0; overflowsInRow < 6 && x < Integer.MAX_VALUE; x++) {
            System.out.print(x + "\t");
            try {
                System.out.println(expression.evaluate(x, 0, 0));
                overflowsInRow = 0;
            } catch (OverflowException e) {
                System.out.println("overflow");
                overflowsInRow++;
            } catch (ZeroDivisionException e) {
                System.out.println("division by zero");
                overflowsInRow = 0;
            } catch (Exception e) {
                System.err.println("unknown Exception :" + e.getLocalizedMessage());
                return;
            }
        }
    }
}
