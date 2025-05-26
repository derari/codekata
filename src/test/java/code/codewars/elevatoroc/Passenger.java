package code.codewars.elevatoroc;

public record Passenger(FloorId destination) {

    public Direction directionFrom(FloorId current) {
        return current.directionTo(destination);
    }

    @Override
    public String toString() {
        return ""+ destination.id();
    }
}
