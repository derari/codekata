package code.kata11;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RackTest {

    @Test
    void test() {
        var rack = new Rack();
        assertThat(rack.getBalls(), is(empty()));
        rack.add(20);
        assertThat(rack.getBalls(), contains(20));
        rack.add(10);
        assertThat(rack.getBalls(), contains(10, 20));
        rack.add(30);
        assertThat(rack.getBalls(), contains(10, 20, 30));
    }

    private static class Rack {

        final BucketSort sort = new BucketSort(60);

        public void add(int value) {
            sort.add(value);
        }

        public List<Integer> getBalls() {
            return sort.stream().boxed().toList();
        }
    }
}
