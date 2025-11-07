package code.codewars.alpinist;

import java.util.List;
import java.util.stream.IntStream;

import static code.codewars.alpinist.Alpinist.intArray;

public record Point(int x, int y) {

    public static List<Point> points(String coordinates) {
        return points(intArray(coordinates));
    }

    public static List<Point> points(int... coordinates) {
        if (coordinates.length % 2 != 0) {
            throw new IllegalArgumentException("Coordinates must be in pairs");
        }
        return IntStream.range(0, coordinates.length / 2)
                .mapToObj(i -> new Point(coordinates[2 * i], coordinates[2 * i + 1]))
                .toList();
    }

    public Point move(Direction direction) {
        return new Point(x + direction.x(), y + direction.y());
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + "}";
    }
}
