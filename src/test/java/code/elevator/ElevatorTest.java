package code.elevator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElevatorTest {

    @Test
    void asDots() {
        assertEquals("", Elevator.asDots(0, 0));
        assertEquals("⡀", Elevator.asDots(1, 0));
        assertEquals("⣀", Elevator.asDots(2, 0));
        assertEquals("⣄", Elevator.asDots(3, 0));
        assertEquals("⣤", Elevator.asDots(4, 0));
        assertEquals("⣦", Elevator.asDots(5, 0));
        assertEquals("⣶", Elevator.asDots(6, 0));
        assertEquals("⣷", Elevator.asDots(7, 0));
        assertEquals("⣿", Elevator.asDots(8, 0));

        assertEquals("⠀", Elevator.asDots(0, 1));
        assertEquals("⠀⠀", Elevator.asDots(0, 2));

        assertEquals("⡀", Elevator.asDots(1, 1));
        assertEquals("⡀⠀", Elevator.asDots(1, 2));

        assertEquals("⣿⣄", Elevator.asDots(11, 1));
        assertEquals("⣿⣄", Elevator.asDots(11, 2));
        assertEquals("⣿⣄⠀", Elevator.asDots(11, 3));
        assertEquals("⣿⣿⣄", Elevator.asDots(19, 2));

        assertEquals("⣿⠀", Elevator.asDots(8, 2));
        assertEquals("⣿⣿", Elevator.asDots(16, 2));
        assertEquals("⣿⣿⣿", Elevator.asDots(24, 2));
        assertEquals("⣿⣿⣿⣿", Elevator.asDots(32, 2));
    }
}
