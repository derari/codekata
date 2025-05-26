package code.kata.kata4;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RefactoredTest {

    @Test
    public void findMaxSpreadDay() throws IOException {
        var data = new TableReader(Path.of("src/test/resources/weather.dat"));
        var value = findMinSpreadValue(data, "Dy", "MxT", "MnT");
        assertThat(value, is("14"));
    }

    @Test
    public void findMinSpreadTeam() throws IOException {
        var data = new TableReader(Path.of("src/test/resources/football.dat"));
        var value = findMinSpreadValue(data, "Team", "F", "A");
        assertThat(value, is("Aston_Villa"));
    }

    private String findMinSpreadValue(TableReader data, String keyColumn, String value1Column, String value2Column) {
        var key = "";
        var minDiff = Float.MAX_VALUE;
        for (var line: data) {
            var a = line.getFloat(value1Column);
            if (a == null) continue;
            var b = line.getFloat(value2Column);
            Objects.requireNonNull(b, value2Column);
            var diff = Math.abs(a - b);
            if (diff < minDiff) {
                minDiff = diff;
                key = line.getString(keyColumn);
            }
        }
        return key;
    }

    static class TableReader implements Iterable<Line> {

        final List<String> lines;
        final Map<String, int[]> columns;

        public TableReader(Path input) throws IOException {
            lines = Files.readAllLines(input);
            columns = parseColumns(lines.remove(0));
        }

        private Map<String, int[]> parseColumns(String headline) {
            var map = new HashMap<String, int[]>();
            var headers = headline.split("\\b(?!\\s)");
            var index = 0;
            for (var header: headers) {
                var end = index + header.length();
                map.put(header.strip(), new int[]{index, end});
                index = end;
            }
            return map;
        }

        private int[] getPosition(String column) {
            var position = columns.get(column);
            if (position == null) throw new IllegalArgumentException("unknown column `" + column + "`");
            return position;
        }

        @Override
        public Iterator<Line> iterator() {
            return lines.stream().map(l -> new Line(this, l)).iterator();
        }
    }

    static class Line {
        private final TableReader reader;
        private final String line;

        public Line(TableReader reader, String line) {
            this.reader = reader;
            this.line = line;
        }

        private <T> T getValue(String column, Function<String, T> parser) {
            var position = reader.getPosition(column);
            if (line.length() <= position[0]) return null;
            var string = line.substring(position[0], Math.min(position[1], line.length()));
            return parser.apply(string.strip());
        }

        public String getString(String column) {
            return getValue(column, string -> string.isEmpty() ? null : string);
        }

        public Float getFloat(String column) {
            return getValue(column, string -> {
                var match = FLOAT_PATTERN.matcher(string);
                if (!match.find()) return null;
                return Float.parseFloat(match.group());
            });
        }

        private static final Pattern FLOAT_PATTERN = Pattern.compile("-?\\s*\\d+(\\.\\d+)?");
    }
}
