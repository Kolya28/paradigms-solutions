package expression.generic.operations;


import java.math.BigInteger;

public class BinIntegerOperations extends GenericOperations<BigInteger> {
    @Override
    public BigInteger parseConstant(String string) {
        return new BigInteger(string);
    }

    @Override
    public boolean isPartOfConstant(char c) {
        return '0' <= c && c <= '9';
    }

    @Override
    public BigInteger valueOf(int val) {
        return BigInteger.valueOf(val);
    }

    @Override
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger subtract(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    @Override
    public BigInteger multiply(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    @Override
    public BigInteger divide(BigInteger a, BigInteger b) {
        return a.divide(b);
    }

    @Override
    public BigInteger negate(BigInteger a) {
        return a.negate();
    }
}
