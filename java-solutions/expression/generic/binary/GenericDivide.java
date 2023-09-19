package expression.generic.binary;

import expression.generic.GenericExpression;
import expression.generic.operations.GenericOperations;

public class GenericDivide<T> extends GenericBinaryOperation<T> {
    public GenericDivide(GenericExpression<T> left, GenericExpression<T> right) {
        super(left, right);
    }

    @Override
    protected String getOperationString() {
        return "/";
    }

    @Override
    protected T compute(T a, T b, GenericOperations<T> operations) {
        return operations.divide(a, b);
    }
}
