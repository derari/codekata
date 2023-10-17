package code.kata16;

import code.kata16.engine.PaymentProcessingState;
import code.kata16.engine.ProcessingRule;

class RulesJsonParserSimplifiedTest extends PaymentHandlerTestBase {

    @Override
    protected PaymentHandler instance(OtherServices services) {
        return new EnginePaymentHandler(engine(), services);
    }

    private ProcessingRule<PaymentProcessingState> engine() {
        return new RulesJsonParser().parse(JSON);
    }

    private static final String JSON = """
            [
              {
                "if": {
                  "productClass": "physical"
                },
                "then": [
                    {
                      "packagingSlipFor": "shipping"
                    },
                    {
                      "if": {
                        "productType": "book"
                      },
                      "then": {
                        "packagingSlipFor": "royalties"
                      }
                    },
                    {
                      "if": {
                        "productType": "video",
                        "productName": "Learning to Ski"
                      },
                      "then": {
                        "action": "ShipExtraItem",
                        "name": "First Aid",
                        "type": "video",
                        "comment": "court decision in 1997"
                      }
                    },
                    {
                      "if": {
                        "test": "HasSalesAgent"
                      },
                      "then": {
                        "action": "GenerateCommission"
                      }
                    }
                  ]
              },
              {
                "if": {
                  "productClass": "membership"
                },
                "then": {
                  "if": {
                    "test": "HasUpgradableMembership"
                  },
                  "then": {
                    "action": "UpgradeMembership"
                  },
                  "else": {
                    "action": "ActivateMembership"
                  }
                }
              },
              {
                "action": "SubmitPackingSlips"
              }
            ]
            """;
}
