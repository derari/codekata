package code.kata2;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Trampoline pattern
 */
public class Chop4Test extends ChopTestBase {

    @Override
    protected int chop(int n, List<Integer> list) {
        Object value = chop(n, list, 0);
        while (value instanceof Supplier<?> supplier) {
            value = supplier.get();
        }
        return (Integer) value;
    }

    private Object chop(int n, List<Integer> list, int offset) {
        if (list.isEmpty()) return -1;
        var i = list.size() / 2;
        var v = list.get(i);
        if (v == n) return offset + i;
        if (v < n) return (Supplier<?>) () -> chop(n, list.subList(i+1, list.size()), offset + i+1);
        return (Supplier<?>) () -> chop(n, list.subList(0, i), offset);
    }

    /*
     Bugs:

        none :-)
     */
}
