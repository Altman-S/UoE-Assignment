package ed.inf.adbs.minibase;

import java.util.Collection;
import java.util.stream.Collectors;

public class Utils {

    public static String join(Collection<?> c, String delimiter) {
        return c.stream()
                .map(x -> x.toString())
                .collect(Collectors.joining(delimiter));
    }
}
