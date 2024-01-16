package code.kata16;

import code.kata16.engine.OrderProcessingState;
import code.kata16.engine.Action;

public class EnginePaymentHandler implements PaymentHandler {

    private final Action<OrderProcessingState> engine;
    private final OtherServices services;

    public EnginePaymentHandler(Action<OrderProcessingState> engine, OtherServices services) {
        this.engine = engine;
        this.services = services;
    }

    @Override
    public void paymentReceived(Order order) {
        log("processing %s", order);
        var state = new OrderProcessingState(this::log, order);
        engine.apply(state, services);
        log("done (%s)", state);
    }

    protected void log(String message, Object... args) {
        System.out.print("\n> ");
        System.out.printf(message, args);
    }
}
