package code.kata9.enterprise;

import java.util.List;

public interface Tally {

    int get(String product);

    void bill(String product, int quantity, int itemPrice);

    void bill(Bill.Item item, int newQuantity);

    interface Update {

        Tally getTotal();

        Tally getUpdate();

        List<Bill.Item> getPreviouslyBilled();

        void clear(String product);
    }
}
