package code.kata.kata19;

import com.sun.source.tree.Tree;

import java.util.*;

public class WordMap {

    private final Map<Integer, Map<String, List<String>>> neighborsBySize = new HashMap<>();
    private final Map<Integer, List<String>> newWordsBySize = new HashMap<>();

    public void add(String word) {
        var newWords = newWordsBySize.computeIfAbsent(word.length(), k -> new ArrayList<>());
        newWords.add(word);
    }

    private void scanNewWords(int size) {
        var newWords = newWordsBySize.remove(size);
        if (newWords == null) return;
        var neighbors = getNeighbors(size);
        for (var word : newWords) {
            var newList = new ArrayList<String>();
            neighbors.forEach((key, list) -> {
                if (isNeighbor(key, word)) {
                    list.add(word);
                    newList.add(key);
                }
            });
            neighbors.put(word, newList);
//            System.out.println(word + " -> " + newList);
        }
    }

    private Map<String, List<String>> getNeighbors(int size) {
        return neighborsBySize.computeIfAbsent(size, k -> new HashMap<>());
    }

    private boolean isNeighbor(String a, String b) {
        if (a.length() != b.length()) return false;
        int distance = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) distance++;
        }
        return distance == 1;
    }

    public List<String> path_v1(String start, String goal) {
//        Benchmark lead -> gold:  0,14614 ms
//        Benchmark gold -> lead:  0,04370 ms
//        Benchmark ruby -> code:  0,29433 ms
//        Benchmark code -> ruby:  6,92083 ms
        if (start.length() != goal.length()) throw new IllegalArgumentException("Words must have the same length");
        if (start.equals(goal)) return List.of(start);
        scanNewWords(start.length());

        var neighbors = getNeighbors(start.length());
        var queue = new ArrayDeque<Step>();
        queue.add(new Step(null, start));

        while (!queue.isEmpty()) {
            var step = queue.poll();
            var next = neighbors.get(step.word);
            for (var word : next) {
                if (word.equals(goal)) return new Step(step, word).toList();
                if (!step.contains(word)) queue.add(new Step(step, word));
            }
        }
        return null;
    }

    public List<String> path_v2(String start, String goal) {
//        Benchmark lead -> gold:  0,08263 ms
//        Benchmark gold -> lead:  0,02889 ms
//        Benchmark ruby -> code:  0,10446 ms
//        Benchmark code -> ruby:  0,77415 ms
        if (start.length() != goal.length()) throw new IllegalArgumentException("Words must have the same length");
        if (start.equals(goal)) return List.of(start);
        scanNewWords(start.length());

        var neighbors = getNeighbors(start.length());
        var queue = new ArrayDeque<Step>();
        var found = new HashSet<String>();
        found.add(start);
        queue.add(new Step(null, start));

        while (!queue.isEmpty()) {
            var step = queue.poll();
            var next = neighbors.get(step.word);
            for (var word : next) {
                if (word.equals(goal)) return new Step(step, word).toList();
                if (found.add(word)) queue.add(new Step(step, word));
            }
        }
        return null;
    }

    public List<String> path(String start, String goal) {
//        Benchmark lead -> gold:  0,00252 ms
//        Benchmark gold -> lead:  0,00223 ms
//        Benchmark ruby -> code:  0,00204 ms
//        Benchmark code -> ruby:  0,00266 ms
        if (start.length() != goal.length()) throw new IllegalArgumentException("Words must have the same length");
        if (start.equals(goal)) return List.of(start);
        scanNewWords(start.length());

        var neighbors = getNeighbors(start.length());
        var queue = new TreeSet<>(BY_SCORE);
        var found = new HashSet<String>();
        var n = 0;
        found.add(start);
        queue.add(new Step(null, start, distance(start, goal), n++));

        while (!queue.isEmpty()) {
            var first = queue.first();
            queue.remove(first);
            var next = neighbors.get(first.word);
            for (var word : next) {
                if (found.add(word)) {
                    var distance = distance(word, goal);
                    if (distance == 0) return new Step(first, word).toList();
                    queue.add(new Step(first, word, distance, n++));
                }
            }
        }
        return null;
    }

    private int distance(String a, String b) {
        int distance = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) distance++;
        }
        return distance;
    }

    private static final Comparator<Step> BY_SCORE = (a, b) -> {
        var distance = a.distance - b.distance;
        var score = distance + a.length - b.length;
        if (score != 0) return score;
        if (distance != 0) return distance;
        return a.unique - b.unique;
    };

    record Step(Step previous, String word, int distance, int length, int unique) {
        
        public Step(Step previous, String word) {
            this(previous, word, -1, -1, -1);
        }
        
        public Step(Step previous, String word, int distance, int unique) {
            this(previous, word, distance, previous == null ? 0 : previous.length + 1, unique);
        }

        public boolean contains(String word) {
            return word.equals(this.word) || (previous != null && previous.contains(word));
        }

        public List<String> toList() {
            var list = new ArrayList<String>();
            var step = this;
            while (step != null) {
                list.add(step.word);
                step = step.previous;
            }
            Collections.reverse(list);
            return list;
        }

        @Override
        public String toString() {
            return word + " (d: " + distance + ", l: " + length + ", u: " + unique + ")";
        }
    }
}
