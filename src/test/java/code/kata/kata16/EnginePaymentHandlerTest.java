package code.kata.kata16;

import code.kata.kata16.engine.*;

class EnginePaymentHandlerTest extends PaymentHandlerTestBase {

    @Override
    protected PaymentHandler instance(OtherServices services) {
        return new EnginePaymentHandler(engine(), services);
    }

    /*
    // initial implementation:
    private ProcessingRule<PaymentProcessingState> engine() {
        return (a, b) -> true;
    }
    */

    private Action<OrderProcessingState> engine() {
        return new Sequence<>(
                new IsProductType(ProductType.PHYSICAL).ifTrue(physicalRules()),
                new IsProductType(ProductType.MEMBERSHIP).ifTrue(membershipRules()),
                new SubmitPackingSlips()
        );
    }

    private Action<OrderProcessingState> physicalRules() {
        return new Sequence<>(
                new GeneratePackingSlip(Department.SHIPPING),
                new IsProductType(ProductType.BOOK)
                        .ifTrue(new GeneratePackingSlip(Department.ROYALTIES)),
                new IsProductType(ProductType.VIDEO).and(new HasProductName("Learning to Ski"))
                        .ifTrue(new Comment<>(new ShipExtraItem("First Aid", ProductType.VIDEO), "court decision in 1997")),
                new HasSalesAgent()
                        .ifTrue(new GenerateCommission()));
    }

    private Action<OrderProcessingState> membershipRules() {
        return new HasUpgradableMembership()
                .then(new UpgradeMembership())
                .orElse(new ActivateMembership());
    }
}
