package code.codewars.elevatoroc;

import java.util.*;

public class ItemLog<E> {

    private final List<E> items = new ArrayList<>();

    public void add(E item) {
        if (items.isEmpty() || !items.getLast().equals(item)) {
            items.add(item);
        }
    }

    public List<E> getItems() {
        return Collections.unmodifiableList(items);
    }
}
