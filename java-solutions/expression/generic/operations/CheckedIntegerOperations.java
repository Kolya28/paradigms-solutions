package expression.generic.operations;

import expression.exceptions.OverflowException;
import expression.exceptions.ZeroDivisionException;

public class CheckedIntegerOperations extends IntegerOperations{
    @Override
    public Integer add(Integer a, Integer b) {
        if (a < 0 && b < 0 && a + b >= 0
                || a > 0 && b > 0 && a + b <= 0) {
            throw new OverflowException("Overflow: " + a + " + " + b);
        }
        return super.add(a, b);
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        if (a <= 0 && b >= 0 && a - b > 0
                || a >= 0 && b <= 0 && a - b < 0) {
            throw new OverflowException("Overflow: " + a + " - " + b);
        }
        return super.subtract(a, b);
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        if (a == 0 || b == 0) {
            return 0;
        }

        int ab = a * b;
        if (ab / a != b || ab / b != a) {
            throw new OverflowException("Overflow: " + a + " + " + b);
        }

        return super.multiply(a, b);
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        if (b == 0) {
            throw new ZeroDivisionException("Division by zero: " + a + " / " + b);
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException("Overflow: " + a + " / " + b);
        }
        return super.divide(a, b);
    }

    @Override
    public Integer negate(Integer a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("Overflow: -(" + a + ")");
        }
        return super.negate(a);
    }
}
