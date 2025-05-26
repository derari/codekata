package code.codewars.elevatoroc;

import java.util.*;

public class PassengerQueue {

    private final List<Passenger> queue = new ArrayList<>();

    public int size() {
        return queue.size();
    }

    public void add(Passenger next) {
        queue.add(next);
    }

    public Optional<Passenger> popArrival(FloorId floorId) {
        var it = queue.iterator();
        while (it.hasNext()) {
            var p = it.next();
            if (p.destination().equals(floorId)) {
                it.remove();
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }
}
