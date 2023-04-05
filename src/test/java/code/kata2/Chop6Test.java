package code.kata2;

import java.util.List;
import java.util.TreeSet;

/**
 * Built in collection
 */
public class Chop6Test extends ChopTestBase {

    @Override
    protected int chop(int n, List<Integer> list) {
        var set = new TreeSet<>(list);
        var head = set.headSet(n, true);
        return head.isEmpty() || head.last() != n ? -1 : head.size() - 1;
    }

    /*
     Bugs:

        none :-)
     */
}
