package expression;

public class ClearBit extends Operation {
    public ClearBit(AnyExpression left, AnyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOperationString() {
        return "clear";
    }

    @Override
    protected int compute(int a, int b) {
        return a & ~(1 << b);
    }
}
