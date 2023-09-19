package expression.exceptions;

import expression.AnyExpression;
import expression.TripleExpression;
import expression.parser.CharSource;
import expression.parser.StringSource;

public class ExpressionParser implements TripleParser {
    @Override
    public TripleExpression parse(String expression) throws ExpressionParseException {
        return new CheckedExpressionParserImpl(new StringSource(expression)).parse();
    }

    private static class CheckedExpressionParserImpl extends expression.parser.ExpressionParser.ExpressionParserImpl {
        public CheckedExpressionParserImpl(CharSource source) {
            super(source);
        }

        @Override
        protected AnyExpression createBinaryOperation(String operation, AnyExpression left, AnyExpression right) {
            return switch (operation) {
                case "+" -> new CheckedAdd(left, right);
                case "-" -> new CheckedSubtract(left, right);
                case "*" -> new CheckedMultiply(left, right);
                case "/" -> new CheckedDivide(left, right);
                default -> super.createBinaryOperation(operation, left, right);
            };
        }

        @Override
        protected AnyExpression createUnaryOperation(String operation, AnyExpression node) {
            return switch (operation) {
                case "-" -> new CheckedNegate(node);
                default -> super.createUnaryOperation(operation, node);
            };
        }

        @Override
        protected IllegalArgumentException error(String message) {
            return new ExpressionParseException(message);
        }
    }
}
