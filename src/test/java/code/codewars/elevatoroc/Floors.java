package code.codewars.elevatoroc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Floors {

    private final List<Floor> floors = new ArrayList<>();
    private boolean freeze = false;

    public Floor get(FloorId fid) {
        var id = fid.id();
        if (id < 0) throw new IllegalArgumentException("id < 0");
        if (freeze && id >= floors.size()) throw new IllegalArgumentException("id >= " + floors.size());
        for (int i = floors.size(); i <= id; i++) {
            floors.add(new Floor(FloorId.valueOf(i)));
        }
        return floors.get(id);
    }

    public void freeze() {
        if (freeze) return;
        floors.stream()
                .flatMap(l -> l.departures().stream())
                .toList()
                .forEach(p -> get(p.destination()));
        freeze = true;
    }

    public Stream<Floor> range(FloorId start, FloorId last) {
        return start.range(last).map(this::get);
    }

    public Stream<Floor> range(FloorId start, Direction direction) {
        if (direction == Direction.UP) {
            var top = FloorId.valueOf(floors.size() - 1);
            if (start.isAbove(top)) return Stream.of();
            return range(start, top);
        }
        var ground = FloorId.valueOf(0);
        if (ground.isAbove(start)) return Stream.of();
        return range(start, ground);
    }

    public boolean hasMoreFloorsWithDepartures(FloorId current, Direction direction) {
        return range(current.next(direction), direction)
                .anyMatch(f -> !f.departures().isEmpty());
    }

    public void print(Function<FloorId, String> elevatorString) {
        range(FloorId.valueOf(floors.size() - 1), Direction.DOWN)
                .forEach(f -> f.println(elevatorString));
        System.out.println();
    }
}
