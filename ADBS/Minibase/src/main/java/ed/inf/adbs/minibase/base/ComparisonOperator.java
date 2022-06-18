package ed.inf.adbs.minibase.base;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum ComparisonOperator {
    EQ("="),
    NEQ("!="),
    GT(">"),
    GEQ(">="),
    LT("<"),
    LEQ("<=");

    private final String text;

    ComparisonOperator(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static ComparisonOperator fromString(String s) throws NoSuchElementException {
        return Arrays.stream(values())
                .filter(op -> op.text.equalsIgnoreCase(s))
                .findFirst().get();
    }

}
