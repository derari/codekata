package code.codewars.elevatoroc;

public class PassengerRoom {

    private final int capacity;
    private final PassengerQueue passengers = new PassengerQueue();

    public PassengerRoom(int capacity) {
        this.capacity = capacity;
    }

    public boolean exit(Floor current) {
        var p = passengers.popArrival(current.id());
        if (p.isEmpty()) {
            return false;
        }
        current.arrivals().add(p.get());
        return true;
    }

    public boolean addNext(Floor current, Direction direction) {
        if (passengers.size() >= capacity) {
            return false;
        }
        var next = current.depart(direction);
        passengers.add(next);
        return true;
    }

    public boolean isEmpty() {
        return passengers.size() == 0;
    }

    public String asDots(int width) {
        return Elevator.asDots(passengers.size(), width);
    }
}
