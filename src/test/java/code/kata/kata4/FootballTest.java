package code.kata.kata4;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FootballTest {

    @Test
    public void findMinSpreadTeam() throws IOException {
        var lines = Files.readAllLines(Path.of("src/test/resources/football.dat"));
        var heading = lines.remove(0);
        var iTeamStart = heading.indexOf("Team");
        var iTeamEnd = heading.indexOf("P");
        var iForStart = heading.indexOf("F");
        var iForEnd = iForStart + 2;
        var iAgainstStart = heading.indexOf("A");
        var iAgainstEnd = iAgainstStart + 2;

        var minTeam = "";
        var minDiff = 99999f;
        for (var line: lines) {
            var nFor = parseFloat(line, iForStart, iForEnd);
            if (nFor == null) continue;
            var nAgainst = parseFloat(line, iAgainstStart, iAgainstEnd);
            Objects.requireNonNull(nAgainst);
            var diff = Math.abs(nFor - nAgainst);
            if (diff < minDiff) {
                minDiff = diff;
                minTeam = line.substring(iTeamStart, iTeamEnd).strip();
            }
        }
        assertThat(minTeam, is("Aston_Villa"));
    }

    private Float parseFloat(String line, int start, int end) {
        var s = line.substring(start, end).replaceAll("[^\\d.]", "");
        if (s.isEmpty()) return null;
        return Float.parseFloat(s);
    }
}
