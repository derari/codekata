package code.kata.kata16;

import code.kata.kata16.engine.OrderProcessingState;
import code.kata.kata16.engine.Action;

public class RulesParserSimplifiedYamlTest extends PaymentHandlerTestBase {

    @Override
    protected PaymentHandler instance(OtherServices services) {
        return new EnginePaymentHandler(engine(), services);
    }

    private Action<OrderProcessingState> engine() {
        return new RulesParser().parseYaml(YAML);
    }

    public static String getPaymentEventYaml() {
        return YAML;
    }

    private static final String YAML = """
            - if:
                productType: physical
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
                productType: membership
              then:
                if:
                  hasUpgradableMembership: false
                then: ActivateMembership
                else: UpgradeMembership
            - SubmitPackingSlips
            """;
}
