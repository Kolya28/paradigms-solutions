package expression;

public class Subtract extends Operation {
    public Subtract(AnyExpression left, AnyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOperationString() {
        return "-";
    }

    @Override
    protected int compute(int a, int b) {
        return a - b;
    }
}
