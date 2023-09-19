package expression.generic;

import expression.generic.operations.GenericOperations;

public interface GenericExpression<T> {
    T evaluate(T x, T y, T z, GenericOperations<T> operations);
}
