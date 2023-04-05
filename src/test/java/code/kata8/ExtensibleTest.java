package code.kata8;

import java.util.*;

public class ExtensibleTest extends TestBase {

    @Override
    protected PairFinder getInstance() {
        return new EfficientImpl();
    }

    static class EfficientImpl implements PairFinder {

        final List<Map<Word, Word>> wordsByLength = new ArrayList<>();

        public EfficientImpl() {
        }

        @Override
        public void add(String word) {
            if (word.length() < 1 || word.length() > 6) return;
            var w = new Word(word);
            getWordsByLength(word.length()).put(w, w);
        }

        private Map<Word, Word> getWordsByLength(int length) {
            for (var i = wordsByLength.size(); i < length; i++)
                wordsByLength.add(new HashMap<>());
            return wordsByLength.get(length - 1);
        }

        @Override
        public List<List<String>> getPairs() {
            var result = new ArrayList<List<String>>();
            for (int i = 1; i < 6; i++)
                collectPairs(i,  result);
            return result;
        }

        private void collectPairs(int word1Length, Collection<List<String>> result) {
            var word1 = new Word(word1Length);
            var word2 = new Word(6 - word1Length);
            var allowedWords1 = getWordsByLength(word1Length);
            var allowedWords2 = getWordsByLength(6 - word1Length);
            getWordsByLength(6).keySet().forEach(fullWord -> {
                word1.read(0, fullWord);
                var found1 = allowedWords1.get(word1);
                if (found1 == null) return;
                word2.read(word1Length, fullWord);
                var found2 = allowedWords2.get(word2);
                if (found2 == null) return;
                assert found1.string != null && found2.string != null;
                result.add(List.of(found1.string, found2.string));
            });
        }
    }

    static class Word {

        final String string;
        final char[] letters;

        Word(String string) {
            this.string = string;
            this.letters = string.toCharArray();
        }

        Word(int length) {
            this.string = null;
            this.letters = new char[length];
        }

        public void read(int position, Word word) {
            assert string == null : "mutable word expected";
            System.arraycopy(word.letters, position, letters, 0, length());
        }

        public int length() {
            return letters.length;
        }

        @Override
        public String toString() {
            return string != null ? string : new String(letters);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Word word)) return false;
            return Arrays.equals(letters, word.letters);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(letters);
        }
    }
}
