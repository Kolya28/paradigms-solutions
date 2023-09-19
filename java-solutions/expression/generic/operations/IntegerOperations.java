package expression.generic.operations;


public class IntegerOperations extends GenericOperations<Integer> {
    @Override
    public Integer parseConstant(String string) {
        return Integer.parseInt(string);
    }

    @Override
    public Integer valueOf(int val) {
        return val;
    }

    @Override
    public boolean isPartOfConstant(char c) {
        return '0' <= c && c <= '9';
    }

    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        return a - b;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        return a / b;
    }

    @Override
    public Integer negate(Integer a) {
        return -a;
    }
}
