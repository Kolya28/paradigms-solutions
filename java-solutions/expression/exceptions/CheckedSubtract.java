package expression.exceptions;

import expression.AnyExpression;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(AnyExpression left, AnyExpression right) {
        super(left, right);
    }

    @Override
    protected int compute(int a, int b) {
        if (a <= 0 && b >= 0 && a - b > 0
                || a >= 0 && b <= 0 && a - b < 0) {
            throw new OverflowException("Overflow: " + a + getOperationString() + b);
        }

        return super.compute(a, b);
    }
}
