package code.codewars.alpinist;

public enum Direction {

    NORTH, EAST, SOUTH, WEST;

    public int x() {
        return switch (this) {
            case EAST -> 1;
            case WEST -> -1;
            default -> 0;
        };
    }

    public int y() {
        return switch (this) {
            case NORTH -> -1;
            case SOUTH -> 1;
            default -> 0;
        };
    }
}
