package expression.generic.operations;

public class ShortOperations extends GenericOperations<Short> {
    @Override
    public Short parseConstant(String string) {
        return Short.parseShort(string);
    }

    @Override
    public Short valueOf(int val) {
        return (short) val;
    }

    @Override
    public boolean isPartOfConstant(char c) {
        return '0' <= c && c <= '9';
    }

    @Override
    public Short add(Short a, Short b) {
        return (short)(a + b);
    }

    @Override
    public Short subtract(Short a, Short b) {
        return (short)(a - b);
    }

    @Override
    public Short multiply(Short a, Short b) {
        return (short)(a * b);
    }

    @Override
    public Short divide(Short a, Short b) {
        return (short)(a / b);
    }

    @Override
    public Short negate(Short a) {
        return (short)(-a);
    }
}
