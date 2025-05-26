package code.kata.kata9.basic;

import java.util.*;

public record DiscountItemRule(String product, int singleItemPrice, SortedMap<Integer, Integer> discounts) implements PriceRule {

    public DiscountItemRule(String product, int singleItemPrice, Map<Integer, Integer> discounts) {
        this(product, singleItemPrice, Collections.unmodifiableSortedMap(new TreeMap<>(discounts)));
        if (discounts.isEmpty()) throw new IllegalArgumentException("Discounts required");
    }

    @Override
    public List<Bill.Item> apply(Map<String, Integer> remainingProducts) {
        var quantity = remainingProducts.remove(product());
        if (quantity == null || quantity == 0) return List.of();
        var result = new ArrayList<Bill.Item>();
        quantity = applyDiscounts(quantity, result);
        if (quantity > 0) {
            result.add(new Bill.Item(product(), quantity, singleItemPrice()));
        }
        return result;
    }

    private int applyDiscounts(int quantity, ArrayList<Bill.Item> result) {
        var min = discounts().firstKey();
        while (quantity >= min) {
            var max = discounts().headMap(quantity + 1).lastKey();
            var discountPrice = discounts().get(max);
            var factor = quantity / max;
            result.add(new Bill.Item(product(), max * factor, discountPrice));
            quantity -= max * factor;
        }
        return quantity;
    }
}
