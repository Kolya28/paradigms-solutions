package expression.exceptions;

import expression.AnyExpression;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(AnyExpression left, AnyExpression right) {
        super(left, right);
    }

    @Override
    protected int compute(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }

        int ab = a * b;
        if (ab / a != b || ab / b != a) {
            throw new OverflowException("Overflow: " + a + getOperationString() + b);
        }

        return ab;
    }
}
