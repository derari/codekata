package code.codewars.elevatoroc;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static code.codewars.elevator.Elevator.asDots;

public record Floor(FloorId id, List<Passenger> departures, List<Passenger> arrivals) {

    public Floor(FloorId id) {
        this(id, new ArrayList<>(), new ArrayList<>());
    }

    public void addDepartures(Integer... destinations) {
        Stream.of(destinations)
                .map(FloorId::valueOf)
                .map(Passenger::new)
                .forEach(departures::add);
    }

    public void addDepartures(Passenger... passengers) {
        departures.addAll(Arrays.asList(passengers));
    }

    public void println(Function<FloorId, String> elevator) {
        System.out.printf("%2d | %s | %s | ", id.id(), asDots(arrivals.size(), 2), elevator.apply(id));
        for (var destination : departures) {
            System.out.print(destination + " ");
        }
        System.out.println();
    }

    public Passenger depart(Direction direction) {
        for (var d: departures) {
            if (d.directionFrom(id) == direction) {
                departures.remove(d);
                return d;
            }
        }
        throw new NoSuchElementException("None waiting");
    }

    public boolean isButtonPressed(Direction direction) {
        for (var d: departures) {
            if (d.directionFrom(id) == direction) {
                return true;
            }
        }
        return false;
    }
}
