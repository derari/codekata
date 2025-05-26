package code.kata.kata14;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.*;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

class MarkovTest {

    private static final Pattern LETTER = Pattern.compile("[\\p{javaAlphabetic}',.;:!?]|\\s+");

    @Test
    void basic2() {
        var instance = new Markov();
        var input = "I wish I may I wish I might";
        instance.load(new StringReader(input));

        for (int i = 0; i < 100; i++) {
            var output = instance.stream()
                    .takeWhile(s -> !".".equals(s))
                    .collect(Collectors.joining(" "));
            assertThat(output, matchesPattern("I wish (I may I wish )*I might"));
        }
    }

    @Test
    void airship2() {
        airship(new Markov(2));
    }

    @Test
    void airship3() {
        airship(new Markov(3));
    }

    @Test
    void airship5() {
        airship(new Markov(5));
    }

    void airship(Markov instance) {
        generate(instance, "airship");
    }

    @Test
    void dachdecker2() {
        dachdecker(new Markov(2, LETTER));
    }

    @Test
    void dachdecker3() {
        dachdecker(new Markov(3, LETTER));
    }

    void dachdecker(Markov instance) {
        generate(instance, "dachdecker");
    }

    void generate(Markov instance, String name) {
        var path = Path.of("src/test/resources/%s.txt".formatted(name));
        try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            instance.load(reader);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        instance.links.forEach((k, v) -> {
            System.out.println(k + " -> " + v);
        });

        int[] counter = {0};
        var output = instance.stream()
                .takeWhile(w -> counter[0]++ < 150 || !Markov.STOP.contains(w))
                .collect(text());
        System.out.println();
        System.out.println(output);
        System.out.println();
    }

    private static Collector<String, StringBuilder, String> text() {
        return new Collector<>() {
            @Override
            public Supplier<StringBuilder> supplier() {
                return StringBuilder::new;
            }

            @Override
            public BiConsumer<StringBuilder, String> accumulator() {
                return (sb, w) -> {
                    if (sb.length() > 0 && Character.isAlphabetic(w.charAt(0))) {
                        sb.append(' ');
                    }
                    sb.append(w);
                    if (Markov.STOP.contains(w)) sb.append("\n");
                };
            }

            @Override
            public BinaryOperator<StringBuilder> combiner() {
                return (sb1, sb2) -> {
                    if (sb2.isEmpty()) return sb1;
                    if (sb1.isEmpty()) return sb2;
                    return sb1.append(' ').append(sb2);
                };
            }

            @Override
            public Function<StringBuilder, String> finisher() {
                return StringBuilder::toString;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Set.of();
            }
        };
    }
}
