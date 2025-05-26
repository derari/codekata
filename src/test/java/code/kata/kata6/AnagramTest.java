package code.kata.kata6;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.System.out;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AnagramTest {

    AnagramDB db = new AnagramDB();

    @Test
    void testSmall() {
        db.addAll("arrest", "rarest", "raster", "raters", "Sartre", "starer");
        db.addAll("boaster", "boaters", "borates");

        assertThat(db.find("boaster"), hasSize(3));
        assertThat(db.find("tsrrea"), hasSize(5));

        assertThat(db.size(), is(2));
        assertThat(db.getLongestWord(), is("boaster"));
        assertThat(db.getLargestSet(), is("arrest"));
    }

    @Test
    void testWordList() throws IOException {
        var words = Files.readAllLines(Path.of("src/test/resources/wordlist.txt"));
        var start = -System.currentTimeMillis();
        words.forEach(db::add);

        var arrest = db.find("tsrrea");
        out.println(arrest);
        assertThat(arrest, hasSize(7));

        assertThat(db.size(), is(20683));

        var longest = db.getLongestWord();
        out.println(db.find(longest));
        assertThat(longest, is("acoustoelectrically"));

        var largest = db.getLargestSet();
        out.println(db.find(largest));
        assertThat(largest, is("alerts"));

        out.printf("Total time: %dms", start + System.currentTimeMillis());
    }

    static class AnagramDB {

        private final char[] buf = new char[64];
        private final Map<String, Set<String>> db = new HashMap<>();

        public void addAll(String... words) {
            Stream.of(words).forEach(this::add);
        }

        public void add(String word) {
            db.computeIfAbsent(key(word), k -> new TreeSet<>()).add(word);
        }

        public Set<String> find(String word) {
            return db.get(key(word));
        }

        private String key(String word) {
            word.getChars(0, word.length(), buf, 0);
            int n = 0;
//            for (int i = 0; i < word.length(); i++) {
//                if (Character.isAlphabetic(buf[i])) {
//                    buf[i - n] = Character.toLowerCase(buf[i]);
//                    continue;
//                }
//                n++;
//            }
            Arrays.sort(buf, 0, word.length() - n);
            return new String(buf, 0, word.length() - n);
        }

        public int size() {
            return (int) db.values().stream()
                    .filter(set -> set.size() > 1)
                    .count();
        }

        public String getLongestWord() {
            return db.values().stream()
                    .filter(strings -> strings.size() > 1)
                    .map(strings -> strings.iterator().next())
                    .max(Comparator.comparing(String::length))
                    .orElse(null);
        }

        public String getLargestSet() {
            return db.values().stream()
                    .max(Comparator.comparing(Set::size))
                    .map(words -> words.iterator().next())
                    .orElse(null);
        }
    }
}
