package code.kata.kata16;

import org.junit.jupiter.api.Test;

import static code.kata.kata16.ProductType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ProductTypeTest {

    @Test
    void is_type() {
        assertThat(BOOK.is(PHYSICAL), is(true));
        assertThat(BOOK.is(MEMBERSHIP), is(false));
        assertThat(GYM_MEMBERSHIP.is(MEMBERSHIP), is(true));
        assertThat(GYM_PREMIUM.is(MEMBERSHIP), is(true));
        assertThat(GYM_PREMIUM.is(GYM_MEMBERSHIP), is(true));
        assertThat(GYM_PREMIUM.is(GYM_BASIC), is(false));
    }

    @Test
    void getUpgradable() {
        assertThat(BOOK.getUpgradable(), emptyIterable());
        assertThat(GYM_PREMIUM.getUpgradable(), contains(GYM_BASIC));
        assertThat(CINEMA_ULTRA.getUpgradable(), contains(CINEMA_BASIC, CINEMA_PREMIUM));
    }
}
