package code.kata.kata2;

import java.util.List;

/**
 * Trampoline pattern
 */
public class Chop4bTest extends ChopTestBase {

    @Override
    protected int chop(int n, List<Integer> list) {
        return chop(n, list, 0, list.size()).value();
    }

    private Trampoline<Integer> chop(int n, List<Integer> list, int start, int end) {
        if (end <= start) return Trampoline.done(-1);
        var i = (start + end) / 2;
        var v = list.get(i);
        if (v == n) return Trampoline.done(i);
        if (v < n) return () -> chop(n, list, i + 1, end);
        return () -> chop(n, list, start, i);
    }

    /*
     Bugs:

        none :-)
     */
}
