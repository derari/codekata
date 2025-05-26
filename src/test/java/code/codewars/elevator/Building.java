package code.codewars.elevator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Building {

    private final List<Level> levels = new ArrayList<>();
    private final Elevator elevator = new Elevator(5, this);
    private boolean freezeLevels = false;

    public Level level(int id) {
        if (id < 0) throw new IllegalArgumentException("id < 0");
        if (freezeLevels && id >= levels.size()) throw new IllegalArgumentException("id >= " + levels.size());
        for (int i = levels.size(); i <= id; i++) {
            levels.add(new Level(i));
        }
        return levels.get(id);
    }

    public Elevator getElevator() {
        return elevator;
    }

    public void runElevator() {
        freezeLevels();
        do {
            print();
        } while (elevator.run());
    }

    private void freezeLevels() {
        if (freezeLevels) return;
        levels.stream()
                .flatMap(l -> l.departures().stream())
                .collect(Collectors.toSet())
                .forEach(this::level);
        freezeLevels = true;
    }

    private void print() {
        for (int i = levels.size() - 1; i >= 0; i--) {
            var elevatorString = elevator.getLevel() == i ? elevator.toString() : "   ";
            levels.get(i).println(elevatorString);
        }
        System.out.println();
    }

    public boolean hasWaiting(int level, int direction) {
        for (level += direction; level >= 0 && level < levels.size(); level += direction) {
            if (!levels.get(level).departures().isEmpty()) return true;
        }
        return false;
    }
}
