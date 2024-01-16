package code.kata17;

import code.kata16.*;
import code.kata16.engine.*;

import java.util.EnumMap;

public class OrderWorkflowRepository {

    private final EnumMap<OrderWorkflowKey, Action<OrderProcessingState>> workflows = new EnumMap<>(OrderWorkflowKey.class);
    private final RulesParser rulesParser = new RulesParser();
    private final OtherServices services;

    public OrderWorkflowRepository(OtherServices services) {
        this.services = services;
    }

    public void addYaml(OrderWorkflowKey key, String yaml) {
        var action = rulesParser.parseYaml(yaml);
        workflows.put(key, action);
    }

    public OrderProcessingState run(OrderWorkflowKey key, OrderState orderState) {
        System.out.println();
        var id = orderState.getId();
        var order = orderState.getOrder();
        log("processing %s %s %s", key, id, order);
        var processingState = new OrderProcessingState(this::log, id, order, orderState::isStarted);
        workflows.get(key).apply(processingState, services);
        log("done (%s)", processingState);
        return processingState;
    }

    protected void log(String message, Object... args) {
        System.out.print("\n> ");
        System.out.printf(message, args);
    }
}
