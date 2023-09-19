package expression.exceptions;

import expression.AnyExpression;
import expression.Divide;

public class CheckedDivide extends Divide {
    public CheckedDivide(AnyExpression left, AnyExpression right) {
        super(left, right);
    }

    @Override
    protected int compute(int a, int b) {
        if (b == 0) {
            throw new ZeroDivisionException("Division by zero: " + a + getOperationString() + b);
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException("Overflow: " + a + getOperationString() + b);
        }
        return super.compute(a, b);
    }
}
