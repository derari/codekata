package code.kata8;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

public class ExtensibleTest extends TestBase {

    @Override
    protected ExtensibleImpl getInstance() {
        return new ExtensibleImpl();
    }

    @Test
    void testSmallWithRange() {
        var db = getInstance();
        db.addAll("al", "bums", "albums");
        db.addAll("bar", "ely", "barely");
        db.addAll("be", "foul", "befoul");
        db.addAll("con", "vex", "convex");
        db.addAll("albe", "confoul");

        var pairs = db.getPairs(5, 7);
        assertThat(pairs, hasItem(List.of("al", "bums")));
        assertThat(pairs, hasItem(List.of("bar", "ely")));
        assertThat(pairs, hasItem(List.of("be", "foul")));
        assertThat(pairs, hasItem(List.of("con", "vex")));
        assertThat(pairs, not(hasItem(List.of("al", "be"))));
        assertThat(pairs, hasItem(List.of("con", "foul")));
    }

    static class ExtensibleImpl implements PairFinder {

        final ItemAggregator<Word> impl;

        public ExtensibleImpl() {
            this.impl = new ItemAggregator<>(Word::new);
        }

        @Override
        public void add(String word) {
            impl.add(word);
        }

        @Override
        public List<List<String>> getPairs() {
            return getPairs(6, 6);
        }

        public List<List<String>> getPairs(int min, int max) {
            return impl.aggregate(
                    word -> word.length() >= min && word.length() <= max,
                    WordPairCollector::new);
        }
    }

    static class ItemAggregator<I> {

        final Map<I, I> items = new HashMap<>();
        final Function<? super String, ? extends I> parser;

        public ItemAggregator(Function<? super String, ? extends I> parser) {
            this.parser = parser;
        }

        public void add(String value) {
            var item = parser.apply(value);
            if (item != null) items.put(item, item);
        }

        public <R> R aggregate(Predicate<? super I> targetFilter, Function<UnaryOperator<I>, Collector<I, ?, R>> collector) {
            return items.values().stream()
                    .filter(targetFilter)
                    .collect(collector.apply(items::get));
        }
    }

    record WordPairCollector(UnaryOperator<Word> lookup) implements Collector<Word, WordPairs, List<List<String>>> {

        @Override
        public Supplier<WordPairs> supplier() {
            return () -> new WordPairs(lookup);
        }

        @Override
        public BiConsumer<WordPairs, Word> accumulator() {
            return WordPairs::add;
        }

        @Override
        public BinaryOperator<WordPairs> combiner() {
            return WordPairs::combine;
        }

        @Override
        public Function<WordPairs, List<List<String>>> finisher() {
            return WordPairs::finish;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of();
        }
    }

    record WordPairs(UnaryOperator<Word> lookup, Word buf, List<List<String>> result) {

        public WordPairs(UnaryOperator<Word> lookup) {
            this(lookup, new Word(64), new ArrayList<>());
        }

        void add(Word word) {
            for (int length1 = 1; length1 < word.length(); length1++) {
                buf.read(word, 0, length1);
                var found1 = lookup.apply(buf);
                if (found1 == null) continue;
                buf.read(word, length1, word.length() - length1);
                var found2 = lookup.apply(buf);
                if (found2 == null) continue;
                result.add(List.of(found1.toString(), found2.toString()));
            }
        }

        WordPairs combine(WordPairs other) {
            result.addAll(other.result);
            return this;
        }

        List<List<String>> finish() {
            return result;
        }
    }

    static class Word {

        final String string;
        final char[] letters;
        int length;
        int hashcode;

        Word(String string) {
            this.string = string;
            this.letters = string.toCharArray();
            this.length = letters.length;
            this.hashcode = computeHashCode();
        }

        Word(int length) {
            this.string = null;
            this.letters = new char[length];
            this.length = length;
        }

        public void read(Word word, int position, int length) {
            assert string == null : "mutable word expected";
            System.arraycopy(word.letters, position, letters, 0, length);
            this.length = length;
            this.hashcode = computeHashCode();
        }

        public int length() {
            return length;
        }

        @Override
        public String toString() {
            return string != null ? string : new String(letters);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Word word)) return false;
            return length() == word.length() &&
                    Arrays.equals(letters, 0, length(),
                            word.letters, 0, length());
        }

        @Override
        public int hashCode() {
            return hashcode;
        }

        private int computeHashCode() {
            int result = 1;
            for (int i = 0; i < length; i++)
                result = 31 * result + letters[i];
            return result;
        }
    }
}
