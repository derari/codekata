package code.kata9.enterprise;

import java.util.List;
import java.util.Map;

public interface PriceRule<T> {

    T newState();

    void update(T state, Tally.Update tally);
}
