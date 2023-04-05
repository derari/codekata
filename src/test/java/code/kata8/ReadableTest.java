package code.kata8;

import java.util.*;

public class ReadableTest extends TestBase {

    @Override
    protected PairFinder getInstance() {
        return new ReadableImpl();
    }

    static class ReadableImpl implements PairFinder {

        final Map<Integer, Set<String>> wordsByLength = new HashMap<>();

        public ReadableImpl() {
        }

        @Override
        public void add(String word) {
            getWordsByLength(word.length()).add(word);
        }

        private Set<String> getWordsByLength(int length) {
            return wordsByLength.computeIfAbsent(length, n -> new TreeSet<>());
        }

        @Override
        public List<List<String>> getPairs() {
            var result = new ArrayList<List<String>>();
            for (int i = 1; i < 6; i++)
                collectPairs(i,  result);
            return result;
        }

        private void collectPairs(int word1Length, Collection<List<String>> result) {
            var allowedWords = getWordsByLength(6);
            getWordsByLength(word1Length).forEach(word1 -> {
                getWordsByLength(6 - word1Length).forEach(word2 -> {
                    var word = word1 + word2;
                    if (allowedWords.contains(word)) {
                        result.add(List.of(word1, word2));
                    }
                });
            });
        }
    }
}
