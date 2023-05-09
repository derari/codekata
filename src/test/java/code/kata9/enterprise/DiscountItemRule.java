package code.kata9.enterprise;

import java.util.*;

public record DiscountItemRule(String product, SortedMap<Integer, Integer> discounts) implements PriceRule {

    public DiscountItemRule(String product, Map<Integer, Integer> discounts) {
        this(product, Collections.unmodifiableSortedMap(new TreeMap<>(discounts)));
        if (discounts.isEmpty()) throw new IllegalArgumentException("Discounts required");
    }

    @Override
    public Object newState() {
        return null;
    }

    @Override
    public void update(Object state, Tally.Update tally) {
        tally.clear(product());
        var quantity = tally.getTotal().get(product());
        var min = discounts().firstKey();
        while (quantity >= min) {
            var max = discounts().headMap(quantity + 1).lastKey();
            var discountPrice = discounts().get(max);
            var factor = quantity / max;
            tally.getTotal().bill(product(), max * factor, discountPrice);
            quantity -= max * factor;
        }
    }
}
