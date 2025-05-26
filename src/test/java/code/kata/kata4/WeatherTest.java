package code.kata.kata4;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WeatherTest {

    final int iDay = 4;
    final int iMax = 10;
    final int iMin = 16;

    @Test
    public void findMaxSpreadDay() throws IOException {
        var lines = Files.readAllLines(Path.of("src/test/resources/weather.dat"));
        var minDay = 0;
        var minDiff = 99999f;
        for (var line: lines.subList(2, lines.size())) {
            var max = parseFloat(line, iDay, iMax);
            var min = parseFloat(line, iMax, iMin);
            var diff = max - min;
            if (diff < minDiff) {
                minDiff = diff;
                minDay = parseInt(line, 0, iDay);
            }
        }
        assertThat(minDay, is(14));
    }

    private int parseInt(String line, int start, int end) {
        var s = line.substring(start, end);
        return Integer.parseInt(s.strip());
    }

    private float parseFloat(String line, int start, int end) {
        var s = line.substring(start, end);
        return Float.parseFloat(s.replaceAll("[^\\d.]", ""));
    }
}
