package expression.exceptions;

public class ExpressionParseException extends IllegalArgumentException {
    public ExpressionParseException() {
        super();
    }

    public ExpressionParseException(String s) {
        super(s);
    }

    public ExpressionParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionParseException(Throwable cause) {
        super(cause);
    }
}
