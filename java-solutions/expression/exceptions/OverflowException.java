package expression.exceptions;

public class OverflowException extends ArithmeticException {
    public OverflowException() {
        super();
    }

    public OverflowException(String s) {
        super(s);
    }
}
