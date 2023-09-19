package expression;

public class Multiply extends Operation {
    public Multiply(AnyExpression left, AnyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOperationString() {
        return "*";
    }

    @Override
    protected int compute(int a, int b) {
        return a * b;
    }
}
