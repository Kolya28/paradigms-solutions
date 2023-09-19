package expression;

public class Add extends Operation {
    public Add(AnyExpression left, AnyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOperationString() {
        return "+";
    }

    @Override
    protected int compute(int a, int b) {
        return a + b;
    }
}
