package expression.generic.operations;

public class FloatOperations extends GenericOperations<Float> {
    @Override
    public Float parseConstant(String string) {
        return Float.parseFloat(string);
    }

    @Override
    public boolean isPartOfConstant(char c) {
        return '0' <= c && c <= '9'
                || c == 'e' || c == '.';
    }

    @Override
    public Float valueOf(int val) {
        return (float) val;
    }

    @Override
    public Float add(Float a, Float b) {
        return a + b;
    }

    @Override
    public Float subtract(Float a, Float b) {
        return a - b;
    }

    @Override
    public Float multiply(Float a, Float b) {
        return a * b;
    }

    @Override
    public Float divide(Float a, Float b) {
        return a / b;
    }

    @Override
    public Float negate(Float a) {
        return -a;
    }
}
