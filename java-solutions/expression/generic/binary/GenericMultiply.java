package expression.generic.binary;

import expression.generic.GenericExpression;
import expression.generic.operations.GenericOperations;

public class GenericMultiply<T> extends GenericBinaryOperation<T> {
    public GenericMultiply(GenericExpression<T> left, GenericExpression<T> right) {
        super(left, right);
    }

    @Override
    protected String getOperationString() {
        return "*";
    }

    @Override
    protected T compute(T a, T b, GenericOperations<T> operations) {
        return operations.multiply(a, b);
    }
}
