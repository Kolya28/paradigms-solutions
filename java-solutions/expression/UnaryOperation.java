package expression;

import java.util.Objects;

public abstract class UnaryOperation implements AnyExpression {
    protected final AnyExpression node;

    protected UnaryOperation(AnyExpression node) {
        this.node = node;
    }

    protected abstract String getOperationString();

    protected abstract int compute(int a);

    @Override
    public int evaluate(int x, int y, int z) {
        return compute(node.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return getOperationString() + "(" + node + ')';
    }

    @Override
    public String toMiniString() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UnaryOperation o)) {
            return false;
        }
        return Objects.equals(node, o.node)
                && Objects.equals(getOperationString(), o.getOperationString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, getOperationString());
    }
}
