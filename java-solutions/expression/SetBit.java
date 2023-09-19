package expression;

public class SetBit extends Operation {
    public SetBit(AnyExpression left, AnyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOperationString() {
        return "set";
    }

    @Override
    protected int compute(int a, int b) {
        return a | (1 << b);
    }
}
