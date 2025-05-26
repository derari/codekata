package code.kata.kata16.engine;

import code.kata.kata16.OtherServices;
import code.kata.kata17.OrderWorkflowKey;

public record ScheduleWorkflow(OrderWorkflowKey workflow) implements Action<OrderProcessingState> {
    @Override
    public boolean apply(OrderProcessingState state, OtherServices services) {
        state.scheduled().add(workflow);
        return true;
    }
}
