package expression.generic;

import java.util.*;
import java.util.stream.Stream;

import expression.generic.binary.*;
import expression.generic.operations.GenericOperations;
import expression.generic.unary.*;
import expression.parser.*;

public final class GenericExpressionParser<T> {
    public GenericExpression<T> parse(String expression, GenericOperations<T> genericOperations) {
        return new ExpressionParserImpl<>(new StringSource(expression), genericOperations).parse();
    }

    public static class ExpressionParserImpl<T> extends BaseParser {
        private final int maxPriority;
        private final int maxOperationLength;
        private final Map<String, Integer> binaryOperationPriorities;
        private final Set<String> supportedUnaryOperations;
        private final GenericOperations<T> genericOperations;
        private String currentOperation = "";

        public ExpressionParserImpl(CharSource source, Map<String, Integer> binaryOperationPriorities,
                                    Set<String> supportedUnaryOperations, GenericOperations<T> genericOperations) {
            super(source);
            this.genericOperations = genericOperations;
            this.supportedUnaryOperations = supportedUnaryOperations;
            this.binaryOperationPriorities = binaryOperationPriorities;
            maxPriority = Collections.max(binaryOperationPriorities.values());
            maxOperationLength = Stream.concat(binaryOperationPriorities.keySet().stream(), supportedUnaryOperations.stream())
                    .max(Comparator.comparingInt(String::length))
                    .orElse("")
                    .length();
        }

        public ExpressionParserImpl(CharSource source, GenericOperations<T> genericOperations) {
            this(source, Map.of(
                            "*", 2,
                            "/", 2,
                            "+", 1,
                            "-", 1,
                            "set", 0,
                            "clear", 0),
                    Set.of("-", "count"), genericOperations);
        }

        // possibility to add new variables and operations by overriding
        // variable names can be longer
        protected boolean testVariable(String name) {
            return name.length() == 1 && 'x' <= name.charAt(0) && name.charAt(0) <= 'z';
        }

        protected GenericExpression<T> createBinaryOperation(String operation, GenericExpression<T> left, GenericExpression<T> right) {
            return switch (operation) {
                case "+" -> new GenericAdd<>(left, right);
                case "-" -> new GenericSubtract<>(left, right);
                case "*" -> new GenericMultiply<>(left, right);
                case "/" -> new GenericDivide<>(left, right);

                default -> throw error("Unknown binary operation: " + operation);
            };
        }

        protected GenericExpression<T> createUnaryOperation(String operation, GenericExpression<T> node) {
            return switch (operation) {
                case "-" -> new GenericNegate<>(node);

                default -> throw error("Unknown unary operation: " + operation);
            };
        }

        public GenericExpression<T> parse() {
            if (take(')')) {
                throw error("Wrong closure sequence");
            }

            GenericExpression<T> expression = parse(0);

            if (test(')') || test('(')) {
                throw error("Wrong closure sequence");
            }
            if (!eof()) {
                throw error("Invalid symbol: '" + currentChar() + '\'');
            }
            return expression;
        }

        private GenericExpression<T> parse(int currentPriority) {
            if (currentPriority > maxPriority) {
                GenericExpression<T> factor = parseUnary();
                takeBinaryOperation();
                return factor;
            }
            GenericExpression<T> expr = parse(currentPriority + 1);
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

        private GenericExpression<T> parseUnary() {
            skipWhitespace();

            if (supportedUnaryOperations.contains("-") && take('-')) {
                GenericExpression<T> number = parseConstant(true);
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
                return new GenericVariable<>(name);
            }

            if (!supportedUnaryOperations.contains(name)) {
                throw error("Unsupported unary operation: " + name);
            }

            return createUnaryOperation(name, parseUnary());
        }

        private GenericExpression<T> parseArgument() {
            skipWhitespace();
            GenericExpression<T> number = parseConstant(false);
            if (number != null) {
                return number;
            }

            skipWhitespace();
            if (take('(')) {
                skipWhitespace();
                GenericExpression<T> expr = parse(0);

                skipWhitespace();
                expect(')');
                return expr;
            }

            throw error("Expected argument, found: '" + (eof() ? "EOF" : currentChar()) + '\'');
        }

        protected GenericExpression<T> parseConstant(boolean negative) {
            final StringBuilder sb = new StringBuilder(negative ? "-" : "");

            if(!genericOperations.isPartOfConstant(currentChar())) {
                return null;
            }

            do {
                sb.append(take());
            } while (genericOperations.isPartOfConstant(currentChar()));

            try {
                return new GenericConst<>(genericOperations.parseConstant(sb.toString()));
            } catch (final NumberFormatException e) {
                throw error("Constant overflow: " + sb);
            }
        }
    }
}