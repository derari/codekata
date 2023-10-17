package code.kata16;

import code.kata16.engine.PaymentProcessingState;
import code.kata16.engine.ProcessingRule;

class RulesJsonParserTest extends PaymentHandlerTestBase {

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
                "action": "Conditional",
                "if": {
                  "test": "IsProductClass",
                  "class": "physical"
                },
                "then": {
                  "steps": [
                    {
                      "action": "GeneratePackingSlip",
                      "department": "shipping"
                    },
                    {
                      "action": "Conditional",
                      "if": {
                        "test": "IsProductType",
                        "type": "book"
                      },
                      "then": {
                        "action": "GeneratePackingSlip",
                        "department": "royalties"
                      }
                    },
                    {
                      "action": "Conditional",
                      "if": {
                        "test": "And",
                        "conditions": [
                          {
                            "test": "IsProductType",
                            "type": "video"
                          },
                          {
                            "test": "HasProductName",
                            "name": "Learning to Ski"
                          }
                        ]
                      },
                      "then": {
                        "action": "ShipExtraItem",
                        "name": "First Aid",
                        "type": "video",
                        "comment": "court decision in 1997"
                      }
                    },
                    {
                      "action": "Conditional",
                      "if": {
                        "test": "HasSalesAgent"
                      },
                      "then": {
                        "action": "GenerateCommission"
                      }
                    }
                  ]
                }
              },
              {
                "action": "Conditional",
                "if": {
                  "test": "IsProductClass",
                  "class": "membership"
                },
                "then": {
                  "action": "Conditional",
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
