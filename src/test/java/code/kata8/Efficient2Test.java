package code.kata8;

import java.util.*;

public class Efficient2Test extends TestBase {

    @Override
    protected PairFinder getInstance() {
        return new EfficientImpl();
    }

    static class EfficientImpl implements PairFinder {

        final List<TreeSet<Word>> wordsByLength = new ArrayList<>();

        public EfficientImpl() {
        }

        @Override
        public void add(String word) {
            if (word.length() < 1 || word.length() > 6) return;
            getWordsByLength(word.length()).add(new Word(word));
        }

        private Set<Word> getWordsByLength(int length) {
            for (var i = wordsByLength.size(); i < length; i++)
                wordsByLength.add(new TreeSet<>());
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
            var allowedWords = getWordsByLength(6).iterator();
            var current = allowedWords.next();
            var buf = new Word(6);
            for (var word1: getWordsByLength(word1Length)) {
                var c1 = current.comparePrefixTo(word1);
                while (c1 < 0) {
                    if (!allowedWords.hasNext()) return;
                    current = allowedWords.next();
                    c1 = current.comparePrefixTo(word1);
                }
                if (c1 != 0) continue;
                buf.write(0, word1);
                for (var word2: getWordsByLength(6 - word1Length)) {
                    if (word1Length + word2.length() != 6) continue;
                    buf.write(word1Length, word2);
                    var c2 = current.compareTo(buf);
                    while (c2 < 0) {
                        if (!allowedWords.hasNext()) return;
                        current = allowedWords.next();
                        c2 = current.compareTo(buf);
                    }
                    if (c2 == 0) result.add(List.of(word1.toString(), word2.toString()));
                }
            }
        }
    }

    static class Word implements Comparable<Word> {

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

        public void write(int position, Word word) {
            assert string == null : "mutable word expected";
            System.arraycopy(word.letters, 0, letters, position, word.length());
        }

        public int length() {
            return letters.length;
        }

        @Override
        public String toString() {
            return string != null ? string : new String(letters);
        }

        @Override
        public int compareTo(Word o) {
            return Arrays.compare(letters, o.letters);
        }

        public int comparePrefixTo(Word o) {
            var length = Math.min(length(), o.length());
            for (var i = 0; i < length; i++) {
                var c = letters[i] - o.letters[i];
                if (c != 0) return c;
            }
            return 0;
        }
    }
}
