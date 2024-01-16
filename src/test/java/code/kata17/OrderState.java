package code.kata17;

import code.kata16.Order;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class OrderState {

    private final long id;
    private final Order order;
    private final EnumSet<OrderWorkflowKey> started = EnumSet.noneOf(OrderWorkflowKey.class);
    private final EnumSet<OrderWorkflowKey> scheduled = EnumSet.noneOf(OrderWorkflowKey.class);

    public OrderState(Order order) {
        id = UNIQUE_ID.getAndIncrement();
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public boolean start(OrderWorkflowKey workflow) {
        scheduled.remove(workflow);
        if (started.contains(OrderWorkflowKey.CANCELLED)) return false;
        return started.add(workflow);
    }

    public boolean schedule(OrderWorkflowKey workflow) {
        if (started.contains(OrderWorkflowKey.CANCELLED)) return false;
        return scheduled.add(workflow);
    }

    public boolean cancel() {
        if (started.contains(OrderWorkflowKey.CANCELLED)) return true;
        if (started.contains(OrderWorkflowKey.SHIPMENT)) return false;
        return start(OrderWorkflowKey.CANCELLED);
    }

    public boolean isStarted(OrderWorkflowKey workflow) {
        return started.contains(workflow);
    }

    public Iterable<OrderWorkflowKey> getScheduled() {
        return ScheduledIterator::new;
    }

    public boolean productsAvailable() {
        return schedule(OrderWorkflowKey.SHIPMENT);
    }

    public boolean paymentReceived() {
        return schedule(OrderWorkflowKey.PAYMENT_RECEIVED);
    }

    private class ScheduledIterator implements Iterator<OrderWorkflowKey> {
        @Override
        public boolean hasNext() {
            return !scheduled.isEmpty();
        }

        @Override
        public OrderWorkflowKey next() {
            var next = scheduled.iterator().next();
            scheduled.remove(next);
            return next;
        }
    }

    private static final AtomicLong UNIQUE_ID = new AtomicLong();
}
