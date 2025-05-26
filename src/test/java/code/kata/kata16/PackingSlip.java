package code.kata.kata16;

import java.util.ArrayList;
import java.util.List;

public record PackingSlip(Customer customer, List<Order> items) {

    public PackingSlip(Order order) {
        this(order.customer(), new ArrayList<>());
        items().add(order);
    }
}
