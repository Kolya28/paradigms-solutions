package expression.exceptions;

public class ZeroDivisionException extends ArithmeticException {
    public ZeroDivisionException() {
        super();
    }

    public ZeroDivisionException(String s) {
        super(s);
    }
}
