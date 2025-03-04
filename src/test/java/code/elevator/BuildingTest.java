package code.elevator;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuildingTest {

    Building instance = new Building();

    @Test
    void runElevator_up() {
        instance.level(0).addDepartures(2);
        instance.runElevator();
        assertStops(0, 2, 0);
    }

    @Test
    void runElevator_upAndDown() {
        instance.level(2).addDepartures(3, 1);
        instance.runElevator();
        assertStops(2, 3, 2, 1, 0);
    }

    @Test
    void runElevator_capacity() {
        instance.level(2).addDepartures(5, 5, 5, 5, 5, 5, 5);
        instance.runElevator();
        assertStops(2, 5, 2, 5, 0);
    }

    @Test
    void runElevator_stopEvenAtCapacity() {
        instance.level(2).addDepartures(5, 5, 5, 5, 5, 5, 5);
        instance.level(3).addDepartures(5);
        instance.runElevator();
        assertStops(2, 3, 5, 2, 3, 5, 0);
    }

    @Test
    void runElevator_smart() {
        instance.level(1).addDepartures(2);
        instance.level(2).addDepartures(1);
        instance.level(3).addDepartures(1);
        instance.runElevator();
        assertStops(1, 2, 3, 2, 1, 0);
    }

    @Test
    void runElevator_smartAndOrdered() {
        instance.level(2).addDepartures(0, 4, 5, 5, 5, 5, 5, 5, 5, 4);
        instance.level(4).addDepartures(1);
        instance.runElevator();
        assertStops(2, 4, 5, 4, 2, 1, 0, 2, 4, 5, 0);
    }

    private void assertStops(Integer... list) {
        assertEquals(Arrays.asList(list), instance.getElevator().getLog());
    }
}
