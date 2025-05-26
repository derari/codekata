package code.kata.kata2;

import java.util.List;

/**
 * Recursive with list slicing
 */
public class Chop3Test extends ChopTestBase {

    @Override
    protected int chop(int n, List<Integer> list) {
        if (list.isEmpty()) return -1;
        int i = list.size() / 2;
        int v = list.get(i);
        if (v == n) return i;
        if (v < n) {
            var j = chop(n, list.subList(i+1, list.size()));
            return j < 0 ? j : i + j + 1;
        }
        return chop(n, list.subList(0, i));
    }

    /**
     Bugs:
       forgot to end recursion
       return n; --> return i;

       return i + 1 + chop... only handles success case

     */
}
