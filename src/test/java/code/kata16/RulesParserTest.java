package code.kata16;

import code.kata16.engine.OrderProcessingState;
import code.kata16.engine.Action;

class RulesParserTest extends PaymentHandlerTestBase {

    @Override
    protected PaymentHandler instance(OtherServices services) {
        return new EnginePaymentHandler(engine(), services);
    }

    private Action<OrderProcessingState> engine() {
        return new RulesParser().parseJson(JSON);
    }

    private static final String JSON = """
            [
              {
                "action": "Conditional",
                "if": {
                  "test": "IsProductType",
                  "type": "physical"
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
                        "action": "Comment",
                        "comment": "court decision in 1997",
                        "rule": {
                          "action": "ShipExtraItem",
                          "name": "First Aid",
                          "type": "video"
                        }
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
                  "test": "IsProductType",
                  "type": "membership"
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
