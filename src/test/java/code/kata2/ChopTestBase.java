package code.kata2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public abstract class ChopTestBase {
    
    protected abstract int chop(int n, List<Integer> list);

    @Test
    public void test0() {
        assertThat(chop(3, List.of()), is(-1));
    }

    @Test
    public void test1() {
        assertThat(chop(3, List.of(1)), is(-1));
        assertThat(chop(1, List.of(1)), is(0));
    }

    @Test
    public void test3() {
        assertThat(chop(1, List.of(1, 3, 5)), is(0));
        assertThat(chop(3, List.of(1, 3, 5)), is(1));
        assertThat(chop(5, List.of(1, 3, 5)), is(2));
        assertThat(chop(0, List.of(1, 3, 5)), is(-1));
        assertThat(chop(2, List.of(1, 3, 5)), is(-1));
        assertThat(chop(4, List.of(1, 3, 5)), is(-1));
    }

    @Test
    public void test4() {
        assertThat(chop(1, List.of(1, 3, 5, 7)), is(0));
        assertThat(chop(3, List.of(1, 3, 5, 7)), is(1));
        assertThat(chop(5, List.of(1, 3, 5, 7)), is(2));
        assertThat(chop(7, List.of(1, 3, 5, 7)), is(3));
        assertThat(chop(0, List.of(1, 3, 5, 7)), is(-1));
        assertThat(chop(2, List.of(1, 3, 5, 7)), is(-1));
        assertThat(chop(6, List.of(1, 3, 5, 7)), is(-1));
        assertThat(chop(8, List.of(1, 3, 5, 7)), is(-1));
    }
}
