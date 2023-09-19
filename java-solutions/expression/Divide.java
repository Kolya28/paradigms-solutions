package expression;

public class Divide extends Operation {
    public Divide(AnyExpression left, AnyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOperationString() {
        return "/";
    }

    @Override
    protected int compute(int a, int b) {
        return a / b;
    }
}
