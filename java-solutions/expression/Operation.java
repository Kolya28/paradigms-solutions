package expression;

import java.util.Objects;

public abstract class Operation implements AnyExpression {
    protected final AnyExpression left, right;

    protected Operation(AnyExpression left, AnyExpression right) {
        this.left = left;
        this.right = right;
    }

    // use String, not char due to the chance of implementation << >> operations in the future
    protected abstract String getOperationString();

    protected abstract int compute(int a, int b);

    @Override
    public int evaluate(int x, int y, int z) {
        return compute(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "(" + left + ' ' + getOperationString() + ' ' + right + ')';
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
        if (!(obj instanceof Operation o)) {
            return false;
        }
        return Objects.equals(left, o.left) && Objects.equals(right, o.right)
                && Objects.equals(getOperationString(), o.getOperationString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, getOperationString(), right);
    }
}
