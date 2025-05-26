package code.kata.kata11;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CharSortTest {

    @Test
    void test() {
        var sorter = new CharSorter();
        sorter.add("""
                When not studying nuclear physics, Bambi likes to play
                beach volleyball.
                """);
        assertThat(sorter.toString(), is("aaaaabbbbcccdeeeeeghhhiiiiklllllllmnnnnooopprsssstttuuvwyyyy"));
    }

    private static class CharSorter {

        private final BucketSort sort = new BucketSort('a', 'z');

        public void add(String text) {
            text.codePoints().forEach(c -> add((char) c));
        }

        public void add(char c) {
            c = Character.toLowerCase(c);
            if ('a' <= c && c <= 'z') sort.add(c);
        }

        public String toString() {
            return sort.stream().collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        }
    }
}
