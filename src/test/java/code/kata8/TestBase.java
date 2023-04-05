package code.kata8;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.System.out;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public abstract class TestBase {

    protected abstract PairFinder getInstance();

    @Test
    void testSmall() {
        var db = getInstance();
        db.addAll("al", "bums", "albums");
        db.addAll("bar", "ely", "barely");
        db.addAll("be", "foul", "befoul");
        db.addAll("con", "vex", "convex");
        db.addAll("albe", "confoul");

        var pairs = db.getPairs();
        assertThat(pairs, hasItem(List.of("al", "bums")));
        assertThat(pairs, hasItem(List.of("bar", "ely")));
        assertThat(pairs, hasItem(List.of("be", "foul")));
        assertThat(pairs, hasItem(List.of("con", "vex")));
        assertThat(pairs, not(hasItem(List.of("al", "be"))));
        assertThat(pairs, not(hasItem(List.of("con", "foul"))));
    }

    @Test
    void testWordList() throws IOException {
        var db = getInstance();
        var words = Files.readAllLines(Path.of("src/test/resources/wordlist.txt"));
        var start = -System.currentTimeMillis();
        words.forEach(db::add);

        var result = db.getPairs();

        out.println();
        out.println(getClass().getSimpleName().replace("Test", ""));
        out.printf("Total time: %dms\n", start + System.currentTimeMillis());
        out.printf("%d Results:\n", result.size());
        out.println(result);
        assertThat(result.size(), is(30599));
    }

    interface PairFinder {

        default void addAll(String... words) {
            Stream.of(words).forEach(this::add);
        }

        void add(String word);

        List<List<String>> getPairs();
    }
}
