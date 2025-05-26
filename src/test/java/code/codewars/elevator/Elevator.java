package code.codewars.elevator;

import java.util.ArrayList;
import java.util.List;

public class Elevator {

    private final Building building;
    private final int capacity;
    private final List<Integer> passengers = new ArrayList<>();
    private final List<Integer> log = new ArrayList<>();

    private int level = 0;
    private int direction = 1;

    public Elevator(int capacity, Building building) {
        this.capacity = capacity;
        this.building = building;
    }

    public int getLevel() {
        return level;
    }

    public boolean run() {
        return arrivalsExit()
                || waitingEnter()
                || keepGoing()
                || changeDirection()
                || returnToGround();
    }

    private boolean changeDirection() {
        direction = -direction;
        if (waitingEnter() || keepGoing()) {
            return true;
        }
        direction = -direction;
        return false;
    }

    private boolean arrivalsExit() {
        var current = building.level(level);
        var it = passengers.iterator();
        while (it.hasNext()) {
            var p = it.next();
            if (p == level) {
                it.remove();
                current.arrivals().add(p);
                logStop();
                return true;
            }
        }
        return false;
    }

    private boolean waitingEnter() {
        var current = building.level(level);
        if (!current.hasWaiting(direction)) {
            return false;
        }
        logStop();
        if (passengers.size() >= capacity) {
            return false;
        }
        var next = current.depart(direction);
        passengers.add(next);
        return true;
    }

    private boolean keepGoing() {
        if (!passengers.isEmpty() || building.hasWaiting(level, direction)) {
            level += direction;
            return true;
        }
        return false;
    }

    private boolean returnToGround() {
        if (level > 0) {
            direction = -1;
            level--;
            return true;
        }
        logStop();
        return false;
    }

    private void logStop() {
        if (log.isEmpty() || log.get(log.size() - 1) != level) {
            log.add(level);
        }
    }

    public List<Integer> getLog() {
        return log;
    }

    @Override
    public String toString() {
        return "[" + asDots(passengers.size(), 1) + "]";
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
}
