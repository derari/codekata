package code.kata.kata9.basic;

import java.util.*;

public record MultiItemRule(Map<String, Map.Entry<Integer, Integer>> combination) implements PriceRule {

    @Override
    public List<Bill.Item> apply(Map<String, Integer> remainingProducts) {
        int factor = getFactor(remainingProducts);
        if (factor == 0) return List.of();
        var result = new ArrayList<Bill.Item>();
        for (var entry: combination().entrySet()) {
            var product = entry.getKey();
            var expectedQuantity = entry.getValue().getKey();
            remainingProducts.merge(product, -factor * expectedQuantity, Integer::sum);
            result.add(new Bill.Item(product, factor * expectedQuantity, entry.getValue().getValue()));
        }
        return result;
    }

    private int getFactor(Map<String, Integer> remainingProducts) {
        var factor = Integer.MAX_VALUE;
        for (var entry: combination().entrySet()) {
            var product = entry.getKey();
            var expectedQuantity = entry.getValue().getKey();
            var quantity = remainingProducts.getOrDefault(product, 0);
            factor = Math.min(factor, quantity / expectedQuantity);
            if (factor == 0) return 0;
        }
        return factor;
    }
}
