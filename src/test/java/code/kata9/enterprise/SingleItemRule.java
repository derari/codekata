package code.kata9.enterprise;

import java.util.*;

public record SingleItemRule(String product, int itemPrice) implements PriceRule<Void> {

    @Override
    public Void newState() {
        return null;
    }

    @Override
    public boolean requireUpdate(Void state, Set<String> updatedProducts) {
        return updatedProducts.contains(product);
    }

    @Override
    public void update(Void state, Tally.Update tally) {
        var quantity = tally.getUpdate().get(product());
        tally.getUpdate().bill(product(), quantity, itemPrice());
    }
}
