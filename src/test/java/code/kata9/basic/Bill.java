package code.kata9.basic;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record Bill(List<Item> items) {

    public int getTotal() {
        return items.stream().mapToInt(Item::getTotalPrice).sum();
    }

    @Override
    public String toString() {
        return "\n"
                + items.stream().map(Objects::toString).collect(Collectors.joining("\n"))
                + "\n==============\n"
                + getTotal();
    }

    public record Item(String product, int quantity, int itemPrice) {

        public int getTotalPrice() {
            return quantity * itemPrice;
        }
    }
}
