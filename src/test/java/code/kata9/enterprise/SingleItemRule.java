package code.kata9.enterprise;

import java.util.List;
import java.util.Map;

public record SingleItemRule(String product, int itemPrice) implements PriceRule {
    @Override
    public Object newState() {
        return null;
    }

    @Override
    public void update(Object state, Tally.Update tally) {
        var quantity = tally.getUpdate().get(product());
        tally.getUpdate().bill(product(), quantity, itemPrice());
    }
}
