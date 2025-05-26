package code.kata.kata9.enterprise;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Checkout {

    private final List<RuleTally<?>> priceRules;
    private final Bill bill = new Bill(new BillItems());

    public Checkout(PriceRule<?>... priceRules) {
        this(List.of(priceRules));
    }

    public Checkout(List<PriceRule<?>> priceRules) {
        this.priceRules = priceRules.stream().map(RuleTally::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Checkout add(String... products) {
        return add(Stream.of(products)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.summingInt(__ -> 1))));
    }

    public Checkout add(String product) {
        return add(Map.of(product, 1));
    }

    public Checkout add(Map<String, Integer> quantities) {
        for (var rule: priceRules) {
            quantities = rule.update(quantities);
            quantities.values().removeIf(n -> n == 0);
            if (quantities.isEmpty()) return this;
        }
        return this;
    }

    public Bill getBill() {
        return bill;
    }

    private class BillItems implements Iterable<Bill.Item> {

        @Override
        public Iterator<Bill.Item> iterator() {
            return Spliterators.iterator(spliterator());
        }

        @Override
        public void forEach(Consumer<? super Bill.Item> action) {
            spliterator().forEachRemaining(action);
        }

        @Override
        public Spliterator<Bill.Item> spliterator() {
            return priceRules.stream()
                    .flatMap(rule -> rule.billed.stream())
                    .filter(item -> item.quantity() != 0)
                    .spliterator();
        }
    }

    private static class RuleTally<T> implements Tally.Update {

        private final PriceRule<T> rule;
        private final T state;
        private final List<Bill.Item> billed = new LinkedList<>();
        private final Total total = new Total();
        private final Update update = new Update();

        public RuleTally(PriceRule<T> rule) {
            this.rule = rule;
            this.state = rule.newState();
        }

        Map<String, Integer> update(Map<String, Integer> updates) {
            if (!rule.requireUpdate(state, updates.keySet())) return updates;
            update.quantities.clear();
            update.quantities.putAll(updates);
            updates.forEach((product, quantity) -> total.quantities.merge(product, quantity, Integer::sum));
            rule.update(state, this);
            return update.quantities;
        }

        @Override
        public List<Bill.Item> getPreviouslyBilled() {
            return Collections.unmodifiableList(billed);
        }

        @Override
        public Total getTotal() {
            return total;
        }

        @Override
        public Update getUpdate() {
            return update;
        }

        @Override
        public void clear(String product) {
            var it = billed.listIterator();
            while (it.hasNext()) {
                var existing = it.next();
                if (!existing.product().equals(product)) continue;
                it.remove();
                update.quantities.merge(product, existing.quantity(), Integer::sum);
            }
        }

        private abstract class TallyBase implements Tally {

            final Map<String, Integer> quantities = new HashMap<>();

            @Override
            public int get(String product) {
                return quantities.getOrDefault(product, 0);
            }

            @Override
            public void bill(String product, int quantity, int itemPrice) {
                bill(new Bill.Item(product, quantity, itemPrice), quantity);
            }

            @Override
            public void bill(Bill.Item item, int newQuantity) {
                var it = billed.listIterator();
                while (it.hasNext()) {
                    var existing = it.next();
                    if (!existing.matchesProductAndPrice(item)) continue;
                    var updated = update(existing, newQuantity);
                    update.quantities.merge(item.product(), existing.quantity() - updated.quantity(), Integer::sum);
                    it.set(updated);
                    return;
                }
                billed.add((item.withQuantity(newQuantity)));
                update.quantities.merge(item.product(), -newQuantity, Integer::sum);
            }

            protected abstract Bill.Item update(Bill.Item existing, int newQuantity);
        }

        private class Total extends TallyBase {
            @Override
            protected Bill.Item update(Bill.Item existing, int newQuantity) {
                return existing.withQuantity(newQuantity);
            }
        }

        private class Update extends TallyBase {

            @Override
            protected Bill.Item update(Bill.Item existing, int newQuantity) {
                return existing.withQuantity(existing.quantity() + newQuantity);
            }

            @Override
            public void bill(String product, int quantity, int itemPrice) {
                if (quantity == 0) return;
                super.bill(product, quantity, itemPrice);
            }

            @Override
            public void bill(Bill.Item item, int newQuantity) {
                if (newQuantity == 0) return;
                super.bill(item, newQuantity);
            }
        }
    }
}
