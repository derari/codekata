package code.codewars.elevator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static code.codewars.elevator.Elevator.asDots;

public record Level(int id, List<Integer> departures, List<Integer> arrivals) {

    public Level(int id) {
        this(id, new ArrayList<>(), new ArrayList<>());
        if (id < 0) throw new IllegalArgumentException("id < 0");
    }

    public void addDepartures(Integer... destinations) {
        departures.addAll(Arrays.asList(destinations));
    }

    public void println(String elevator) {
        System.out.printf("%2d | %s | %s | ", id, asDots(arrivals.size(), 2), elevator);
        for (var destination : departures) {
            System.out.print(destination + " ");
        }
        System.out.println();
    }

    public int depart(int direction) {
        for (var d: departures) {
            if (direction < 0 ^ d > id) {
                departures.remove(d);
                return d;
            }
        }
        throw new IllegalStateException("None waiting");
    }

    public boolean hasWaiting(int direction) {
        for (var d: departures) {
            if (direction < 0 ^ d > id) {
                return true;
            }
        }
        return false;
    }
}
