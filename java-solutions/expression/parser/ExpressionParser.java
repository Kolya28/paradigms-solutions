package expression.parser;

import expression.*;

import java.util.*;
import java.util.stream.Stream;

public final class ExpressionParser {
    public TripleExpression parse(String expression) {
        return new ExpressionParserImpl(new StringSource(expression)).parse();
    }

    public static class ExpressionParserImpl extends BaseParser {
        private final int maxPriority;
        private final int maxOperationLength;
        private final Map<String, Integer> binaryOperationPriorities;
        private final Set<String> supportedUnaryOperations;
        private String currentOperation = "";

        public ExpressionParserImpl(CharSource source, Map<String, Integer> binaryOperationPriorities,
                                    Set<String> supportedUnaryOperations) {
            super(source);
            this.supportedUnaryOperations = supportedUnaryOperations;
            this.binaryOperationPriorities = binaryOperationPriorities;
            maxPriority = Collections.max(binaryOperationPriorities.values());
            maxOperationLength = Stream.concat(binaryOperationPriorities.keySet().stream(), supportedUnaryOperations.stream())
                    .max(Comparator.comparingInt(String::length))
                    .orElse("")
                    .length();
        }

        public ExpressionParserImpl(CharSource source) {
            this(source, Map.of(
                            "*", 2,
                            "/", 2,
                            "+", 1,
                            "-", 1,
                            "set", 0,
                            "clear", 0),
                    Set.of("-", "count"));
        }

        // possibility to add new variables and operations by overriding
        // variable names can be longer
        protected boolean testVariable(String name) {
            return name.length() == 1 && 'x' <= name.charAt(0) && name.charAt(0) <= 'z';
        }

        protected AnyExpression createBinaryOperation(String operation, AnyExpression left, AnyExpression right) {
            return switch (operation) {
                case "+" -> new Add(left, right);
                case "-" -> new Subtract(left, right);
                case "*" -> new Multiply(left, right);
                case "/" -> new Divide(left, right);
                case "set" -> new SetBit(left, right);
                case "clear" -> new ClearBit(left, right);

                default -> throw error("Unknown binary operation: " + operation);
            };
        }

        protected AnyExpression createUnaryOperation(String operation, AnyExpression node) {
            return switch (operation) {
                case "count" -> new Count(node);
                case "-" -> new Negate(node);

                default -> throw error("Unknown unary operation: " + operation);
            };
        }

        public AnyExpression parse() {
            if (take(')')) {
                throw error("Wrong closure sequence");
            }

            AnyExpression expression = parse(0);

            if (test(')') || test('(')) {
                throw error("Wrong closure sequence");
            }
            if (!eof()) {
                throw error("Invalid symbol: '" + currentChar() + '\'');
            }
            return expression;
        }

        private AnyExpression parse(int currentPriority) {
            if (currentPriority > maxPriority) {
                AnyExpression factor = parseUnary();
                takeBinaryOperation();
                return factor;
            }
            AnyExpression expr = parse(currentPriority + 1);
            while (!currentOperation.isEmpty() && binaryOperationPriorities.get(currentOperation) == currentPriority) {
                String operation = currentOperation;
                expr = createBinaryOperation(operation, expr, parse(currentPriority + 1));
            }
            return expr;
        }

        private String takeOperationName() {
            StringBuilder sb = new StringBuilder();
            if (!eof() && Character.isJavaIdentifierStart(currentChar())) {
                do {
                    sb.append(take());
                    if (sb.length() > maxOperationLength) {
                        throw error("Operation name too long: " + sb);
                    }
                } while (!eof() && Character.isJavaIdentifierPart(currentChar()));
            }
            return sb.toString();
        }

        private void takeBinaryOperation() {
            skipWhitespace();
            if (binaryOperationPriorities.containsKey(Character.toString(currentChar()))) {
                currentOperation = Character.toString(take());
                return;
            }

            currentOperation = takeOperationName();
            if (currentOperation.isEmpty()) {
                return;
            }

            if (!binaryOperationPriorities.containsKey(currentOperation)) {
                throw error("Unknown binary operation: " + currentOperation);
            }
        }

        private AnyExpression parseUnary() {
            skipWhitespace();

            if (supportedUnaryOperations.contains("-") && take('-')) {
                AnyExpression number = parseConstant(true);
                return number != null ? number : createUnaryOperation("-", parseUnary());
            }

            if (binaryOperationPriorities.containsKey(Character.toString(currentChar()))) {
                return createUnaryOperation(Character.toString(take()), parseUnary());
            }

            String name = takeOperationName();
            if (name.isEmpty()) {
                return parseArgument();
            }

            if (testVariable(name)) {
                return new Variable(name);
            }

            if (!supportedUnaryOperations.contains(name)) {
                throw error("Unsupported unary operation: " + name);
            }

            return createUnaryOperation(name, parseUnary());
        }

        private AnyExpression parseArgument() {
            skipWhitespace();
            AnyExpression number = parseConstant(false);
            if (number != null) {
                return number;
            }

            skipWhitespace();
            if (take('(')) {
                skipWhitespace();
                AnyExpression expr = parse(0);

                skipWhitespace();
                expect(')');
                return expr;
            }

            throw error("Expected argument, found: '" + (eof() ? "EOF" : currentChar()) + '\'');
        }

        private AnyExpression parseConstant(boolean negative) {
            final StringBuilder sb = new StringBuilder(negative ? "-" : "");

            if (take('0')) {
                sb.append('0');
            } else if (between('1', '9')) {
                do {
                    sb.append(take());
                } while (between('0', '9'));
            } else {
                return null;
            }

            try {
                return new Const(Integer.parseInt(sb.toString()));
            } catch (final NumberFormatException e) {
                throw error("Constant overflow: " + sb);
            }
        }
    }
}