package code.kata.kata9.basic;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CheckoutTest {

    @Test
    void testSingle() {
        var instance = new Checkout(
                new SingleItemRule("apple", 100),
                new SingleItemRule("banana", 150)
        );
        instance.add("apple", "apple", "banana");
        var bill = instance.getBill();
        System.out.println(bill);
        assertThat(bill.getTotal(), is(350));
    }

    @Test
    void testDiscount() {
        var instance = new Checkout(
                new DiscountItemRule("apple", 100, Map.of(2, 75, 4, 60)),
                new DiscountItemRule("banana", 150, Map.of(2, 200))
        );
        instance.add("apple", "apple", "apple", "banana");
        var bill = instance.getBill();
        System.out.println(bill);
        assertThat(bill.getTotal(), is(400));
    }

    @Test
    void testCombo() {
        var instance = new Checkout(
                new MultiItemRule(Map.of("apple", Map.entry(2, 75), "banana", Map.entry(1, 75))),
                new DiscountItemRule("apple", 100, Map.of(2, 75, 4, 60)),
                new DiscountItemRule("banana", 150, Map.of(2, 200))
        );
        instance.add("apple", "apple", "apple", "banana");
        var bill = instance.getBill();
        System.out.println(bill);
        assertThat(bill.getTotal(), is(325));
    }
}
