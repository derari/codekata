package code.codewars.elevatoroc;

public enum Direction {

    UP {
        @Override
        public int apply(int i) {
            return i + 1;
        }
    },
    DOWN {
        @Override
        public int apply(int i) {
            return i - 1;
        }
    };

    public abstract int apply(int i);

    public Direction reverse() {
        return this == UP ? DOWN : UP;
    }

    public static Direction get(int i) {
        return i > 0 ? UP : DOWN;
    }
}
