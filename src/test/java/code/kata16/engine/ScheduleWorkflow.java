package code.kata16.engine;

import code.kata16.OtherServices;
import code.kata17.OrderWorkflowKey;

public record ScheduleWorkflow(OrderWorkflowKey workflow) implements Action<OrderProcessingState> {
    @Override
    public boolean apply(OrderProcessingState state, OtherServices services) {
        state.scheduled().add(workflow);
        return true;
    }
}
