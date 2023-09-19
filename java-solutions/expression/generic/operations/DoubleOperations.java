package expression.generic.operations;


public class DoubleOperations extends GenericOperations<Double> {
    @Override
    public Double parseConstant(String string) {
        return Double.parseDouble(string);
    }

    @Override
    public boolean isPartOfConstant(char c) {
        return '0' <= c && c <= '9'
                || c == 'e' || c == '.';
    }

    @Override
    public Double valueOf(int val) {
        return (double) val;
    }

    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double subtract(Double a, Double b) {
        return a - b;
    }

    @Override
    public Double multiply(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double divide(Double a, Double b) {
        return a / b;
    }

    @Override
    public Double negate(Double a) {
        return -a;
    }
}
