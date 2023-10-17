package code.kata16;

import code.kata16.engine.PaymentProcessingState;
import code.kata16.engine.ProcessingRule;

public class EnginePaymentHandler implements PaymentHandler {

    private final ProcessingRule<PaymentProcessingState> engine;
    private final OtherServices services;

    public EnginePaymentHandler(ProcessingRule<PaymentProcessingState> engine, OtherServices services) {
        this.engine = engine;
        this.services = services;
    }

    @Override
    public void paymentReceived(Order order) {
        log("processing %s", order);
        var state = new PaymentProcessingState(this::log, order);
        engine.apply(state, services);
        log("done (%s)", state);
    }

    protected void log(String message, Object... args) {
        System.out.print("\n> ");
        System.out.printf(message, args);
    }
}
