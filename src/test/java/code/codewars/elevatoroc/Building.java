package code.codewars.elevatoroc;

public class Building {

    private final Floors floors = new Floors();
    private final Elevator elevator = new Elevator(5);

    public Floor floor(int id) {
        return floors.get(FloorId.valueOf(id));
    }

    public Elevator getElevator() {
        return elevator;
    }

    public void runElevator() {
        floors.freeze();
        do {
            print();
        } while (elevator.run(floors));
    }

    private void print() {
        floors.print(id -> elevator.getCurrentFloor().equals(id) ? elevator.toString() : "   ");
        System.out.println();
    }
}
