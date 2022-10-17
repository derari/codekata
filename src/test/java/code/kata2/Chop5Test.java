package code.kata2;

import java.util.List;
import java.util.TreeSet;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Chop5Test extends ChopTestBase {

    @Override
    protected int chop(int n, List<Integer> list) {
        return chop(n, list, i -> i);
    }

    protected int chop(int n, List<Integer> list, IntUnaryOperator callback) {
        if (list.isEmpty()) return -1;
        var i = list.size() / 2;
        var v = list.get(i);
        if (v == n) return callback.applyAsInt(i);
        if (v > n) return chop(n, list.subList(i+1, list.size()), j -> callback.applyAsInt(j + i + 1));
        return chop(n, list.subList(0, i), callback);
    }
}
