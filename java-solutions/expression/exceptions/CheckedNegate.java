package expression.exceptions;

import expression.AnyExpression;
import expression.Negate;

public class CheckedNegate extends Negate {
    public CheckedNegate(AnyExpression node) {
        super(node);
    }

    @Override
    protected int compute(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("Overflow: -(" + a + ")");
        }
        return super.compute(a);
    }
}
