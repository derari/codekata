package code.kata.kata9.enterprise;

import java.util.List;

public interface Tally {

    int get(String product);

    void bill(String product, int quantity, int itemPrice);

    void bill(Bill.Item item, int newQuantity);

    interface Update {

        Tally getTotal();

        Tally getUpdate();

        List<Bill.Item> getPreviouslyBilled();

        default void clear(String product) {
            var previous = getPreviouslyBilled().stream()
                    .filter(item -> item.product().equals(product))
                    .toList();
            previous.forEach(item -> getTotal().bill(item, 0));
        }
    }
}
