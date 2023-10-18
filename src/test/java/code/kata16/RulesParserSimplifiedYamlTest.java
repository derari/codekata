package code.kata16;

import code.kata16.engine.PaymentProcessingState;
import code.kata16.engine.Action;

class RulesParserSimplifiedYamlTest extends PaymentHandlerTestBase {

    @Override
    protected PaymentHandler instance(OtherServices services) {
        return new EnginePaymentHandler(engine(), services);
    }

    private Action<PaymentProcessingState> engine() {
        return new RulesParser().parseYaml(YAML);
    }

    private static final String YAML = """
            - if:
                productClass: physical
              then:
                - packagingSlipFor: shipping
                - if:
                    productType: book
                  then:
                    packagingSlipFor: royalties
                - if:
                    productType: video
                    productName: Learning to Ski
                  then:
                    comment: court decision in 1997
                    shipExtraItem:
                      type: video
                      name: First Aid
                - if: HasSalesAgent
                  then: GenerateCommission
            - if:
                productClass: membership
              then:
                if:
                  hasUpgradableMembership: false
                then: ActivateMembership
                else: UpgradeMembership
            - SubmitPackingSlips
            """;
}
