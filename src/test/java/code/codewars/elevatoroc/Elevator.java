package code.codewars.elevatoroc;

public class Elevator {

    private final PassengerRoom passengers;
    private final ItemLog<FloorId> log = new ItemLog<>();
    private final State state = new State();

    public Elevator(int capacity) {
        this.passengers = new PassengerRoom(capacity);
    }

    public FloorId getCurrentFloor() {
        return state.currentFloor;
    }

    public boolean run(Floors floors) {
        return arrivalsExit(floors)
                || waitingEnter(floors)
                || keepGoing(floors)
                || changeDirection(floors)
                || returnToGround(floors);
    }

    private boolean changeDirection(Floors floors) {
        state.reverseDirection();
        if (waitingEnter(floors) || keepGoing(floors)) {
            return true;
        }
        state.reverseDirection();
        return false;
    }

    private boolean arrivalsExit(Floors floors) {
        var current = floors.get(state.currentFloor);
        var changed = passengers.exit(current);
        if (changed) {
            logStop();
        }
        return changed;
    }

    private boolean waitingEnter(Floors floors) {
        var current = floors.get(state.currentFloor);
        if (!current.isButtonPressed(state.direction)) {
            return false;
        }
        logStop();
        return passengers.addNext(current, state.direction);
    }

    private boolean keepGoing(Floors floors) {
        if (!passengers.isEmpty() || floors.hasMoreFloorsWithDepartures(state.currentFloor, state.direction)) {
            state.nextFloor();
            return true;
        }
        return false;
    }

    private boolean returnToGround(Floors floors) {
        if (state.returnToGround()) {
            return true;
        }
        logStop();
        return false;
    }

    private void logStop() {
        log.add(state.currentFloor);
    }

    public ItemLog<FloorId> getLog() {
        return log;
    }

    @Override
    public String toString() {
        return "[" + passengers.asDots(1) + "]";
    }

    public static String asDots(int n, int width) {
        var sb = new StringBuilder(width);
        for (int i = 0; i < n / 8; i++) {
            sb.append((char) (0x2800 + DOTS[8]));
        }
        if (n % 8 > 0) {
            sb.append((char) (0x2800 + DOTS[n % 8]));
        }
        while (sb.length() < width) {
            sb.append((char) 0x2800);
        }
        return sb.toString();
    }

    private static final int[] DOTS = {0, 0x40, 0xc0, 0xc4, 0xe4, 0xe6, 0xf6, 0xf7, 0xff};

    private static class State {

        private FloorId currentFloor = FloorId.valueOf(0);
        private Direction direction = Direction.UP;

        public void reverseDirection() {
            direction = direction.reverse();
        }

        public void nextFloor() {
            currentFloor = currentFloor.next(direction);
        }

        public boolean returnToGround() {
            if (!currentFloor.isGround()) {
                direction = Direction.DOWN;
                currentFloor = currentFloor.next(direction);
                return true;
            }
            return false;
        }
    }
}
