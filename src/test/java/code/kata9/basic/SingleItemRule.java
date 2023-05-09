package code.kata9.basic;

import java.util.List;
import java.util.Map;

public record SingleItemRule(String product, int itemPrice) implements PriceRule {

    @Override
    public List<Bill.Item> apply(Map<String, Integer> remainingProducts) {
        var quantity = remainingProducts.remove(product());
        if (quantity == null || quantity == 0) return List.of();
        return List.of(new Bill.Item(product(), quantity, itemPrice()));
    }
}
