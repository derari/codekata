package code.kata14;

import java.io.Reader;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Markov implements Iterable<String> {

    private final Map<String, String> pool = new HashMap<>();
    final Map<Tuple, Link> links = new HashMap<>();

    private final int length;
    private final Pattern pattern;

    public Markov() {
        this(2);
    }

    public Markov(int length) {
        this(length, TOKEN);
    }

    public Markov(int length, Pattern pattern) {
        this.length = length;
        this.pattern = pattern;
    }

    public void load(Reader input) {
        var memory = new Tuple(length);
        new Scanner(input).findAll(pattern).forEach(word -> add(word.group(), memory));
        if (!memory.isEmpty()) {
            add(".", memory);
        }
    }

    private void add(String word, Tuple memory) {
        if (word.isBlank()) word = " ";
        word = pool.computeIfAbsent(word, Function.identity());
        link(memory, word);
        if (STOP.contains(word)) {
            memory.clear();
            return;
        }
        memory.push(word);
    }

    private void link(Tuple key, String word) {
        var link = links.get(key);
        if (link == null) {
            link = new Link();
            links.put(key.copy(), link);
        }
        link.add(word);
    }

    @Override
    public Iterator<String> iterator() {
        return stream().iterator();
    }

    public Stream<String> stream() {
        var memory = new Tuple(length);
        return Stream.generate(() -> next(memory));
    }

    private String next(Tuple memory) {
        var link = links.get(memory);
        if (link == null) {
            memory.clear();
            link = links.get(memory);
        }
        var next = link.any();
        memory.push(next);
        return next;
    }

    private static final Pattern TOKEN = Pattern.compile("Mr\\.|Mrs\\.|[\\p{IsAlphabetic}\\d']+|[,.;:!?]");
    public static final Set<String> STOP = Set.of(".", "!", "?");

    static class Tuple {

        private final String[] values;
        private int head;
        private int length;

        public Tuple(int capacity) {
            head = 0;
            length = 0;
            values = new String[capacity];
        }

        private Tuple(Tuple src) {
            this.head = 0;
            this.length = src.length;
            this.values = new String[length];
            int len1 = Math.min(src.length, src.values.length - src.head);
            System.arraycopy(src.values, src.head, values, 0, len1);
            System.arraycopy(src.values, 0, values, len1, length - len1);
        }

        public Tuple copy() {
            return new Tuple(this);
        }

        public boolean isEmpty() {
            return length == 0;
        }

        public void push(String next) {
            if (length == values.length) {
                values[head] = next;
                head = (head + 1) % length;
                return;
            }
            assert head == 0 : "expect new buffer";
            values[length++] = next;
        }

        public void clear() {
            head = 0;
            length = 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Tuple tuple)) return false;
            if (length != tuple.length) return false;
            for (int i = 0; i < length; i++) {
                if (!get(i).equals(tuple.get(i))) return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            var n = 1;
            for (int i = 0; i < length; i++) {
                n = n * 17 + get(i).hashCode();
            }
            return n;
        }

        private String get(int n) {
            return values[(head + n) % values.length];
        }

        @Override
        public String toString() {
            return Arrays.toString(values);
        }
    }

    static class Link {

        private final Map<String, Integer> weights = new HashMap<>();
        private int total;

        public void add(String word) {
            weights.compute(word, (k, n) -> n == null ? 1 : n + 1);
            total++;
        }

        public String any() {
            var n = ThreadLocalRandom.current().nextInt(total);
            for (var e: weights.entrySet()) {
                n -= e.getValue();
                if (n < 0) return e.getKey();
            }
            throw new IllegalArgumentException(n + " remaining of " + weights);
        }

        @Override
        public String toString() {
            return weights.toString();
        }
    }
}
