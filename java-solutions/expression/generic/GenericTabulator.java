package expression.generic;

import expression.generic.operations.*;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        GenericOperations<?> genericOperations = switch (mode) {
            case "i" -> new CheckedIntegerOperations();
            case "d" -> new DoubleOperations();
            case "bi" -> new BinIntegerOperations();
            case "u" -> new IntegerOperations();
            case "s" -> new ShortOperations();
            case "f" -> new FloatOperations();
            default -> throw new RuntimeException("Unsupported mode " + mode);
        };

        GenericExpressionRoot<?> parsed = genericOperations.parse(expression);

        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        result[i - x1][j - y1][k - z1] = parsed.evaluate(i, j, k);
                    } catch (ArithmeticException ex) {
                        result[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return result;
    }
}
