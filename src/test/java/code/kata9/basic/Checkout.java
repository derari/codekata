package code.kata9.basic;

import java.util.*;
import java.util.stream.Stream;

public class Checkout {

    private final List<PriceRule> priceRules;
    private final Map<String, Integer> items = new HashMap<>();

    public Checkout(PriceRule... priceRules) {
        this(List.of(priceRules));
    }

    public Checkout(List<PriceRule> priceRules) {
        this.priceRules = priceRules;
    }

    public Checkout add(String... products) {
        Stream.of(products).forEach(this::add);
        return this;
    }

    public Checkout add(String product) {
        items.merge(product, 1, Integer::sum);
        return this;
    }

    public Bill getBill() {
        var remaining = new HashMap<>(items);
        var items = new ArrayList<Bill.Item>();
        for (var rule: priceRules) {
            items.addAll(rule.apply(remaining));
            remaining.values().removeIf(count -> count == 0);
            if (remaining.isEmpty()) return new Bill(items);
        }
        throw new UnsupportedOperationException("Unsupported products: " + remaining);
    }
}
