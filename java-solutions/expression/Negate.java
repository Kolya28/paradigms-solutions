package expression;

public class Negate extends UnaryOperation {
    public Negate(AnyExpression node) {
        super(node);
    }

    @Override
    protected String getOperationString() {
        return "-";
    }

    @Override
    protected int compute(int a) {
        return -a;
    }
}
