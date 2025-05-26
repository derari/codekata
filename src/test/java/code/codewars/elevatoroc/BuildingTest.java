package code.codewars.elevatoroc;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuildingTest {

    Building instance = new Building();

    @Test
    void runElevator_up() {
        instance.floor(0).addDepartures(2);
        instance.runElevator();
        assertStops(0, 2, 0);
    }

    @Test
    void runElevator_upAndDown() {
        instance.floor(2).addDepartures(3, 1);
        instance.runElevator();
        assertStops(2, 3, 2, 1, 0);
    }

    @Test
    void runElevator_capacity() {
        instance.floor(2).addDepartures(5, 5, 5, 5, 5, 5, 5);
        instance.runElevator();
        assertStops(2, 5, 2, 5, 0);
    }

    @Test
    void runElevator_stopEvenAtCapacity() {
        instance.floor(2).addDepartures(5, 5, 5, 5, 5, 5, 5);
        instance.floor(3).addDepartures(5);
        instance.runElevator();
        assertStops(2, 3, 5, 2, 3, 5, 0);
    }

    @Test
    void runElevator_smart() {
        instance.floor(1).addDepartures(2);
        instance.floor(2).addDepartures(1);
        instance.floor(3).addDepartures(1);
        instance.runElevator();
        assertStops(1, 2, 3, 2, 1, 0);
    }

    @Test
    void runElevator_smartAndOrdered() {
        instance.floor(2).addDepartures(0, 4, 5, 5, 5, 5, 5, 5, 5, 4);
        instance.floor(4).addDepartures(1);
        instance.runElevator();
        assertStops(2, 4, 5, 4, 2, 1, 0, 2, 4, 5, 0);
    }

    private void assertStops(Integer... list) {
        var expected = Stream.of(list)
                .map(FloorId::valueOf)
                .toList();
        assertEquals(expected, instance.getElevator().getLog().getItems());
    }
}
