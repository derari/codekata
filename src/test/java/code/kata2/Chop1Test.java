package code.kata2;

import java.util.List;

public class Chop1Test extends ChopTestBase {
    
    protected int chop(int n, List<Integer> list) {
        return chop(n, list, 0, list.size());
    }

    private int chop(int n, List<Integer> list, int left, int right) {
        if (left >= right) return -1;
        int i = (left + right) / 2;
        int v = list.get(i);
        if (v == n) return i;
        if (v < n) return chop(n, list, i+1, right);
        return chop(n, list, left, i);
    }

    /*

    Bugs:
       if (v > n)

     */
}
