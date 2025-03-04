package code.kata2;

public interface Trampoline<T> {

    default boolean isDone() {
        return false;
    }

    Trampoline<T> bounce();

    default T value() {
        var trampoline = bounce();
        while (!trampoline.isDone()) {
            trampoline = trampoline.bounce();
        }
        return trampoline.value();
    }

    static <T> Trampoline<T> done(T value) {
        return new Done<>(value);
    }

    record Done<T>(T value) implements Trampoline<T> {
        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public Trampoline<T> bounce() {
            throw new IllegalStateException("Done");
        }
    }
}
