package code.kata.kata16.engine;

import code.kata.kata16.*;
import code.kata.kata17.OrderWorkflowKey;

import java.util.*;
import java.util.function.*;

public record OrderProcessingState(
        BiConsumer<String, Object[]> logger,
        long id,
        Order order,
        PackingSlip packingSlip,
        Set<Department> packingSlipDestinations,
        Set<OrderWorkflowKey> scheduled,
        Predicate<OrderWorkflowKey> isStarted
) implements State {

    public OrderProcessingState(BiConsumer<String, Object[]> logger, Order order) {
        this(logger, -1, order, any -> false);
    }

    public OrderProcessingState(BiConsumer<String, Object[]> logger, long id, Order order, Predicate<OrderWorkflowKey> isStarted) {
        this(logger, id, order, new PackingSlip(order), new LinkedHashSet<>(), EnumSet.noneOf(OrderWorkflowKey.class), isStarted);
    }

    public boolean isStarted(OrderWorkflowKey workflow) {
        return scheduled.contains(workflow) || isStarted.test(workflow);
    }

    public void log(String message, Object... args) {
        logger().accept(message, args);
    }
}
