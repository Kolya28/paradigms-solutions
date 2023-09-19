package expression;

public class Const implements AnyExpression {
    @Override
    public String toMiniString() {
        return AnyExpression.super.toMiniString();
    }

    private final int c;

    public Const(int c) {
        this.c = c;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return c;
    }

    @Override
    public String toString() {
        return Integer.toString(c);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this ||
                obj instanceof Const o && c == o.c;
    }

    @Override
    public int hashCode() {
        return c;
    }
}
