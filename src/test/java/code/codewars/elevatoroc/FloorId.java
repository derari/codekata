package code.codewars.elevatoroc;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public record FloorId(int id) {

    public static FloorId valueOf(int id) {
        return VALUES[id + 1];
    }

    public FloorId next(Direction direction) {
        return valueOf(direction.apply(id));
    }

    public Direction directionTo(FloorId target) {
        return Direction.get(target.id() - id());
    }

    public Stream<FloorId> range(FloorId last) {
        var direction = directionTo(last);
        return Stream.iterate(this, f -> f.next(direction))
                .limit(Math.abs(last.id() - id) + 1);
    }

    public boolean isGround() {
        return id == 0;
    }

    public boolean isAbove(FloorId other) {
        return id > other.id();
    }

    private static final FloorId[] VALUES = new FloorId[20];

    static {
        IntStream.range(0, VALUES.length)
                .forEach(i -> VALUES[i] = new FloorId(i - 1));
    }
}
