package code.kata13;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class JavaLoc1Test extends JavaLocTestBase {
    @Override
    protected JavaLoc loc() {
        return java -> new Impl(java).count();
    }

    static class Impl {

        final String[] lines;
        int currentLine = 0;
        Scope scope = Scope.WHITESPACE;
        int position;
        int count = 0;

        public Impl(String java) {
            this.lines = java.split("[\\r\\n]+");
        }

        public int count() {
            while (currentLine < lines.length) {
                if (countLine()) count++;
                currentLine++;
            }
            return count;
        }

        private boolean countLine() {
            position = 0;
            var foundCode = false;
            while (position < lines[currentLine].length()) {
                scope = scan();
                foundCode |= scope == Scope.CODE;
            }
            return foundCode;
        }

        private Scope scan() {
            var start = position;
            position = find().end() + scope.terminator;
            var last = scope;
            var eol = position > lines[currentLine].length() ? scope.eol() : Scope.WHITESPACE;
            scope = nextScope(eol);
            if (start == position && last == scope) throw new IllegalArgumentException("stuck at " + currentLine + ":" + position + " (" + scope + ")");
            return scope;
        }

        private Matcher find() {
            var pattern = scope.pattern;
            var matcher = pattern.matcher(lines[currentLine]);
            if (!matcher.find(position)) throw new IllegalArgumentException(pattern + " at " + currentLine + ":" + position + " (" + scope + ")");
            return matcher;
        }

        private Scope nextScope(Scope eol) {
            var line = lines[currentLine];
            if (position >= line.length()) return eol;
            if (line.regionMatches(position, "//", 0, 2)) {
                return Scope.COMMENT_EOL;
            }
            if (line.regionMatches(position, "/*", 0, 2)) {
                position += 2;
                return Scope.COMMENT_ML;
            }
            var c = line.charAt(position);
            if (c == '"') {
                position++;
                return Scope.STRING;
            }
            if (Character.isWhitespace(c)) {
                return Scope.WHITESPACE;
            }
            return Scope.CODE;
        }
    }

    enum Scope {
        COMMENT_ML(P_COMMENT_ML, true, 2),
        COMMENT_EOL(P_EOL),
        STRING(P_STRING),
        WHITESPACE(P_WHITESPACE),
        CODE(P_CODE);

        private final Pattern pattern;
        private final boolean multiline;
        private final int terminator;

        Scope(Pattern pattern) {
            this(pattern, false, 0);
        }

        Scope(Pattern pattern, boolean multiline, int terminator) {
            this.pattern = pattern;
            this.multiline = multiline;
            this.terminator = terminator;
        }

        Scope eol() {
            return multiline ? this : WHITESPACE;
        }
    }

    private static final Pattern P_WHITESPACE = Pattern.compile("\\s*");
    private static final Pattern P_CODE = Pattern.compile("([^\"/]|/(?![/*]))*");
    private static final Pattern P_EOL = Pattern.compile(".*");
    private static final Pattern P_COMMENT_ML = Pattern.compile("([^*]|\\*(?!/))*");
    private static final Pattern P_STRING = Pattern.compile("([^\"\\\\]|\\\\.)*\"?");
}
