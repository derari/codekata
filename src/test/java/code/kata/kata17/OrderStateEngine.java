package code.kata.kata17;

import code.kata.kata16.Order;

import java.util.*;

public class OrderStateEngine {

    private final Map<Long, OrderState> orders = new HashMap<>();
    private final OrderWorkflowRepository workflowRepository;

    public OrderStateEngine(OrderWorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    public long create(Order order) {
        var state = new OrderState(order);
        orders.put(state.getId(), state);
        state.schedule(OrderWorkflowKey.NEW);
        return state.getId();
    }

    public boolean cancel(long id) {
        return orders.get(id).cancel();
    }

    public boolean productsAvailable(long id) {
        return orders.get(id).productsAvailable();
    }

    public boolean paymentReceived(long id) {
        return orders.get(id).paymentReceived();
    }

    public boolean isStarted(long id, OrderWorkflowKey workflow) {
        return orders.get(id).isStarted(workflow);
    }

    public void runAll() {
        for (var state: new ArrayList<>(orders.values())) {
            for (var event: state.getScheduled()) {
                if (state.start(event)) {
                    var result = workflowRepository.run(event, state);
                    result.scheduled().forEach(state::schedule);
                }
            }
        }
    }
}
