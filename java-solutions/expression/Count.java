package expression;

public class Count extends UnaryOperation {
    public Count(AnyExpression node) {
        super(node);
    }

    @Override
    protected String getOperationString() {
        return "count";
    }

    @Override
    protected int compute(int a) {
        return Integer.bitCount(a);
    }
}
