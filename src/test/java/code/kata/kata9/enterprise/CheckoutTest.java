package code.kata.kata9.enterprise;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CheckoutTest {

    @Test
    void testSingle() {
        var instance = new Checkout(
                new SingleItemRule("apple", 100),
                new SingleItemRule("banana", 150)
        );
        var bill = instance.getBill();

        instance.add("apple");
        System.out.println(bill);
        assertThat(bill.getTotal(), is(100));

        instance.add("apple", "banana");
        System.out.println(bill);
        assertThat(bill.getTotal(), is(350));
    }

    @Test
    void testDiscount() {
        var instance = new Checkout(
                new DiscountItemRule("apple", Map.of(2, 75, 4, 60)),
                new DiscountItemRule("banana",Map.of(2, 100)),
                new SingleItemRule("apple", 100),
                new SingleItemRule("banana", 150)
        );
        var bill = instance.getBill();

        instance.add("apple");
        System.out.println(bill);
        assertThat(bill.getTotal(), is(100));

        instance.add("apple", "banana", "banana");
        System.out.println(bill);
        assertThat(bill.getTotal(), is(350));
    }
}
