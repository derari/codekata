package code.kata.kata18;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class DependencyGraphTest {

    DependencyGraph<String> dependencies = new DependencyGraph<>();

    void setUpScenario() {
        dependencies.add("A", set("B C"));
        dependencies.add("B", set("C E"));
        dependencies.add("C", set("G"));
        dependencies.add("D", set("A F"));
        dependencies.add("E", set("F"));
        dependencies.add("F", set("H"));
    }

    @Test
    void get_direct() {
        dependencies.add("A", "B", "C");
        assertEquals(set("B C"), dependencies.getAll("A"));
        assertEquals(Set.of(), dependencies.getAll("X"));
    }

    @Test
    void get_transitive() {
        setUpScenario();
        assertEquals(set("B C E F G H"), dependencies.getAll("A"));
        assertEquals(set("C E F G H"), dependencies.getAll("B"));
        assertEquals(set("G"), dependencies.getAll("C"));
        assertEquals(set("A B C E F G H"), dependencies.getAll("D"));
        assertEquals(set("F H"), dependencies.getAll("E"));
        assertEquals(set("H"), dependencies.getAll("F"));
    }

    @Test
    void add_failsOnCircle() {
        dependencies.add("A", "B");
        dependencies.add("B", "C");
        var ex = assertThrows(RuntimeException.class, () -> dependencies.add("C", "A"));
        assertTrue(ex.getMessage().contains("circular"));
    }

    @Test
    void stream_cancelled() {
        var log = new KeyLog();
        setUpScenario();
        dependencies.add("H", "X0");
        for (int i = 0; i < 1000; i++) {
            dependencies.add("X" + i, "X" + (i+1));
        }
        dependencies.setTracer(log);

        var cut = new AtomicInteger(3);
        var list = dependencies.streamAll("A")
                .takeWhile(s -> cut.getAndDecrement() > 0)
                .toList();
        assertEquals(3, list.size());
        assertFalse(log.log.contains("X10"));
    }

    @Test
    void stream2_cancelled() {
        var log = new KeyLog();
        setUpScenario();
        dependencies.add("H", "X0");
        for (int i = 0; i < 1000; i++) {
            dependencies.add("X" + i, "X" + (i+1));
        }
        dependencies.setTracer(log);

        var cut = new AtomicInteger(3);
        var list = dependencies.streamAll2(List.of("A"))
                .takeWhile(s -> cut.getAndDecrement() > 0)
                .toList();
        assertEquals(3, list.size());
        assertFalse(log.log.contains("X10"));
    }

    private static Set<String> set(String chars) {
        return Set.of(chars.split(" "));
    }

    public static final class KeyLog implements DependencyGraph.Tracer<String> {

        private final List<String> log = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void beforeCollect(String key) {
            log.add(key);
            System.out.println("--- BEFORE: " + key);
        }

        @Override
        public void afterCollect(String key) {
            System.out.println("--- AFTER:  " + key);
        }
    }

}