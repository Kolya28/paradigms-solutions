package expression.generic.operations;

import expression.generic.GenericExpression;
import expression.generic.GenericExpressionParser;
import expression.generic.GenericExpressionRoot;

public abstract class GenericOperations<T> {
    public abstract T parseConstant(String string);
    public abstract boolean isPartOfConstant(char c);

    public GenericExpressionRoot<T> parse(String expression) {
        GenericExpressionParser<T> parser = new GenericExpressionParser<>();
        GenericExpression<T> parsedExpression = parser.parse(expression, this);
        return new GenericExpressionRoot<>(parsedExpression, this);
    }

    public abstract T valueOf(int val);
    public abstract T add(T a, T b);
    public abstract T subtract(T a, T b);
    public abstract T multiply(T a, T b);
    public abstract T divide(T a, T b);
    public abstract T negate(T a);
}
