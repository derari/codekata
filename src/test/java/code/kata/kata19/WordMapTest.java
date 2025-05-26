package code.kata.kata19;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordMapTest {

    WordMap instance = new WordMap();

    @Test
    void cat_dog() throws IOException {
        loadWordList();
        var path = instance.path("cat", "dog");
        assertEquals(4, path.size());
        assertEquals("cat", path.get(0));
        assertEquals("cot", path.get(1));
        assertEquals("cog", path.get(2));
        assertEquals("dog", path.get(3));
    }

    @Test
    void lead_gold() throws IOException {
        loadWordList();
        var path = instance.path("lead", "gold");
        assertEquals(4, path.size());
        assertEquals("lead", path.get(0));
        assertEquals("load", path.get(1));
        assertEquals("goad", path.get(2));
        assertEquals("gold", path.get(3));

        benchmark("lead", "gold");
        benchmark("gold", "lead");

        benchmark("ruby", "code");
        benchmark("code", "ruby");
    }

    private void benchmark(String start, String goal) {
        var loops = 1000000L;
        var startNanos = System.nanoTime();
        for (int i = 0; i < loops; i++) {
            instance.path(start, goal);
        }
        var endNanos = System.nanoTime();
        System.out.printf("Benchmark %s -> %s: %8.5f ms%n", start, goal, (endNanos - startNanos) / (loops * 1_000_000d));
    }

    private void loadWordList() throws IOException {
        Files.readAllLines(Path.of("src/test/resources/wordlist.txt")).forEach(instance::add);
    }
}
