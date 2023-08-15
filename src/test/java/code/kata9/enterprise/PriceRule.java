package code.kata9.enterprise;

import java.util.*;

public interface PriceRule<T> {

    T newState();

    default boolean requireUpdate(T state, Set<String> updatedProducts) {
        return true;
    }

    void update(T state, Tally.Update tally);
}
