package expression.generic.unary;

import expression.generic.GenericExpression;
import expression.generic.operations.GenericOperations;

public class GenericNegate<T> extends GenericUnaryOperation<T> {
    public GenericNegate(GenericExpression<T> node) {
        super(node);
    }

    @Override
    protected String getOperationString() {
        return "-";
    }

    @Override
    protected T compute(T a, GenericOperations<T> operations) {
        return operations.negate(a);
    }
}
