package code.kata2;

import java.util.List;

/**
 * Basic iterative algorithm
 */
public class Chop2Test extends ChopTestBase {

    protected int chop(int n, List<Integer> list) {
        var left = 0;
        var right = list.size();
        while (left < right) {
            int i = (left + right) / 2;
            int v = list.get(i);
            if (v == n) return i;
            if (v < n) {
                left = i + 1;
            } else {
                right = i;
            }
        }
        return -1;
    }

    /*

    Bugs:
        none :-)
     */
}
