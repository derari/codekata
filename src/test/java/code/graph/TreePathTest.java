package code.graph;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TreePathTest {

    @Test
    void test1() {
        var tree = new Tree<>(
                List.of(0, 1), List.of(1, 2), List.of(2, 3), List.of(3, 4), List.of(3, 5),
                List.of(0, 11), List.of(11, 12), List.of(12, 13), List.of(13, 14), List.of(13, 15)
        );

        assertEquals(List.of(3), tree.path(3, 3));
        assertEquals(List.of(4, 3, 5), tree.path(4, 5));
        assertEquals(List.of(5, 3, 4), tree.path(5, 4));
        assertEquals(List.of(1, 2, 3, 5), tree.path(1, 5));
        assertEquals(List.of(5, 3, 2, 1), tree.path(5, 1));
        assertEquals(List.of(2, 1, 0, 11, 12, 13, 15), tree.path(2, 15));
        assertEquals(List.of(15, 13, 12, 11, 0, 1, 2), tree.path(15, 2));
        assertEquals(List.of(5, 3, 2, 1, 0, 11, 12), tree.path(5, 12));
        assertEquals(List.of(12, 11, 0, 1, 2, 3, 5), tree.path(12, 5));
    }

    @Test
    void test2() {
        var tree = new Tree<>(
                List.of(0, 1), List.of(1, 2), List.of(2, 3), List.of(3, 4), List.of(3, 5),
                List.of(10, 11), List.of(11, 12), List.of(12, 13), List.of(13, 14), List.of(13, 15)
        );

        var ex = assertThrows(IllegalArgumentException.class, () -> tree.path(3, 15));

        assertEquals("No path from 3 to 15", ex.getMessage());
    }

    record Tree<T>(Map<T, T> parent) {

        @SafeVarargs
        public Tree(List<T>... edges) {
            this(Arrays.asList(edges));
        }

        public Tree(List<List<T>> edges) {
            this(new HashMap<>());
            edges.forEach(e -> parent.put(e.get(1), e.get(0)));
        }

        public List<T> path(T a, T b) {
            var visited = new HashSet<T>();
            var ancestor = a;
            while (ancestor != null) {
                visited.add(ancestor);
                ancestor = parent.get(ancestor);
            }
            ancestor = b;
            while (ancestor != null) {
                if (!visited.add(ancestor)) {
                    return path(a, ancestor, b);
                }
                ancestor = parent.get(ancestor);
            }
            throw new IllegalArgumentException("No path from " + a + " to " + b);
        }

        private List<T> path(T a, T top, T b) {
            var pathB = new ArrayList<T>();
            while (!Objects.equals(b, top)) {
                pathB.add(b);
                b = parent.get(b);
            }
            var result = new ArrayList<T>();
            while (!Objects.equals(a, top)) {
                result.add(a);
                a = parent.get(a);
            }
            result.add(top);
            Collections.reverse(pathB);
            result.addAll(pathB);
            return result;
        }
    }
}
