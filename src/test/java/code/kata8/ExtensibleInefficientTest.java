package code.kata8;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ExtensibleInefficientTest extends TestBase {

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

    @Test
    void testSmallReverse() {
        var db = getInstance();
        db.addAll("ah", "ha", "are", "era", "aha");
        db.addAll("bus", "sub", "bob");
        db.addAll("aloha");

        var pairs = db.getReversible('a');
        assertThat(pairs, hasItems("ha", "era", "aha"));
        assertThat(pairs, not(hasItem("are")));
        assertThat(pairs, not(hasItem("bus")));
        assertThat(pairs, not(hasItem("aloha")));
    }

    static class ExtensibleImpl implements PairFinder {

        final ItemAggregator<Word> items = new ItemAggregator<>();

        @Override
        public void add(String word) {
            items.add(new Word(word));
        }

        @Override
        public List<List<String>> getPairs() {
            return getPairs(6, 6);
        }

        public List<List<String>> getPairs(int min, int max) {
            return items.aggregate(
                    word -> word.length() >= min && word.length() <= max,
                    WordCollector.factory(WordPairs::new));
        }

        public List<String> getReversible(char first) {
            return items.aggregate(
                    word -> word.length() > 0 && word.current.charAt(0) == first,
                    WordCollector.factory(WordReverse::new));
        }
    }

    static class ItemAggregator<I> {

        final Map<I, I> items = new HashMap<>();

        public void add(I value) {
            items.put(value, value);
        }

        public <R> R aggregate(Predicate<? super I> targetFilter, Function<UnaryOperator<I>, ? extends Collector<I, ?, R>> collectorFactory) {
            return aggregate(lookup -> Collectors.filtering(targetFilter, collectorFactory.apply(lookup)));
        }

        public <R> R aggregate(Function<UnaryOperator<I>, ? extends Collector<I, ?, R>> collectorFactory) {
            return items.values().parallelStream()
                    .collect(collectorFactory.apply(items::get));
        }
    }

    static class WordCollector<R> implements Collector<Word, WordAccumulator<R>, List<R>> {

        static <R> Function<UnaryOperator<Word>, WordCollector<R>> factory(Function<? super UnaryOperator<Word>, ? extends WordAccumulator<R>> newAccumulator) {
            return lookup -> new WordCollector<>(newAccumulator, lookup);
        }

        protected Function<? super UnaryOperator<Word>, ? extends WordAccumulator<R>> newAccumulator;
        protected UnaryOperator<Word> lookup;

        public WordCollector(Function<? super UnaryOperator<Word>, ? extends WordAccumulator<R>> newAccumulator, UnaryOperator<Word> lookup) {
            this.newAccumulator = newAccumulator;
            this.lookup = lookup;
        }

        @Override
        public Supplier<WordAccumulator<R>> supplier() {
            return () -> newAccumulator.apply(lookup);
        }

        @Override
        public BiConsumer<WordAccumulator<R>, Word> accumulator() {
            return WordAccumulator::add;
        }

        @Override
        public BinaryOperator<WordAccumulator<R>> combiner() {
            return WordAccumulator::combine;
        }

        @Override
        public Function<WordAccumulator<R>, List<R>> finisher() {
            return WordAccumulator::finish;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of();
        }
    }

    static abstract class WordAccumulator<R> {

        protected final UnaryOperator<Word> lookup;
        protected final Word buf = new Word(64);
        protected final List<R> result = new ArrayList<>();

        public WordAccumulator(UnaryOperator<Word> lookup) {
            this.lookup = lookup;
        }

        abstract void add(Word word);

        WordAccumulator<R> combine(WordAccumulator<R> other) {
            result.addAll(other.result);
            return this;
        }

        List<R> finish() {
            return result;
        }
    }

    static class WordPairs extends WordAccumulator<List<String>> {

        public WordPairs(UnaryOperator<Word> lookup) {
            super(lookup);
        }

        @Override
        void add(Word word) {
            buf.read(word);
            for (int length1 = 1; length1 < word.length(); length1++) {
                buf.setRange(0, length1);
                var found1 = lookup.apply(buf);
                if (found1 == null) continue;
                buf.setRange(length1, word.length() - length1);
                var found2 = lookup.apply(buf);
                if (found2 == null) continue;
                result.add(List.of(found1.toString(), found2.toString()));
            }
        }
    }

    static class WordReverse extends WordAccumulator<String> {

        public WordReverse(UnaryOperator<Word> lookup) {
            super(lookup);
        }

        @Override
        void add(Word word) {
            buf.read(word);
            buf.reverse();
            var found = lookup.apply(buf);
            if (found == null) return;
            result.add(found.toString());
        }
    }

    static class Word {

        final String string;
        String base;
        String current;

        Word(String string) {
            this.string = string;
            this.current = string;
        }

        Word(int length) {
            this.string = null;
            this.current = "";
        }

        public void read(Word word) {
            assert string == null : "mutable word expected";
            base = word.toString();
            current = base;
        }

        public void setRange(int start, int length) {
            current = base.substring(start, start + length);
        }

        public void reverse() {
            var letters = current.toCharArray();
            for (int i = 0; i < length() / 2; i++) {
                char c = letters[i];
                letters[i] = letters[length() - i - 1];
                letters[length() - i - 1] = c;
            }
            current = new String(letters);
        }

        public int length() {
            return current.length();
        }

        @Override
        public String toString() {
            if (string != null) return string;
            return current;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Word word)) return false;
            return current.equals(word.current);
        }

        @Override
        public int hashCode() {
            return current.hashCode();
        }
    }
}
