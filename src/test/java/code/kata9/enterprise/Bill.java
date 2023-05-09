package code.kata9.enterprise;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public record Bill(Iterable<Item> items) {

    public int getTotal() {
        return StreamSupport.stream(items().spliterator(), false).mapToInt(Item::getTotalPrice).sum();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder("\n");
        var total = 0;
        for (var item: items()) {
            sb.append(item).append("\n");
            total += item.getTotalPrice();
        }
        return sb.append("==============\n").append(total).toString();
    }

    public record Item(String product, int quantity, int itemPrice) {

        public int getTotalPrice() {
            return quantity() * itemPrice();
        }

        public boolean matchesProductAndPrice(Item other) {
            return product().equals(other.product())
                    && itemPrice() == other.itemPrice();
        }

        public Item withQuantity(int newQuantity) {
            if (quantity() == newQuantity) return this;
            return new Item(product(), newQuantity, itemPrice());
        }
    }
}
