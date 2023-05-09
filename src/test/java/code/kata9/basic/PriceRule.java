package code.kata9.basic;

import java.util.List;
import java.util.Map;

public interface PriceRule {

    List<Bill.Item> apply(Map<String, Integer> remainingProducts);
}
