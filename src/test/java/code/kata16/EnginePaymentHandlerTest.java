package code.kata16;

import code.kata16.engine.*;

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

    private ProcessingRule<PaymentProcessingState> engine() {
        return new Sequence<>(
                new IsProductClass(ProductClass.PHYSICAL).ifTrue(physicalRules()),
                new IsProductClass(ProductClass.MEMBERSHIP).ifTrue(membershipRules()),
                new SubmitPackingSlips()
        );
    }

    private ProcessingRule<PaymentProcessingState> physicalRules() {
        return new Sequence<>(
                new GeneratePackingSlip(Department.SHIPPING),
                new IsProductType(ProductType.BOOK)
                        .ifTrue(new GeneratePackingSlip(Department.ROYALTIES)),
                new IsProductType(ProductType.VIDEO).and(new HasProductName("Learning to Ski"))
                        .ifTrue(new ShipExtraItem("First Aid", ProductType.VIDEO, "court decision in 1997")),
                new HasSalesAgent()
                        .ifTrue(new GenerateCommission()));
    }

    private ProcessingRule<PaymentProcessingState> membershipRules() {
        return new HasUpgradableMembership()
                .then(new UpgradeMembership())
                .orElse(new ActivateMembership());
    }
}
