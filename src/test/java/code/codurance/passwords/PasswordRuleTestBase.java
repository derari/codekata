package code.codurance.passwords;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class PasswordRuleTestBase {

    protected abstract PasswordRule instance();

    @Test
    void isValid() {
        assertTrue(instance().isValid("Password1!"));
    }

    @Test
    void isValid_noUppercase() {
        assertFalse(instance().isValid("password1!"));
    }

    @Test
    void isValid_noDigit() {
        assertFalse(instance().isValid("Password!"));
    }

    @Test
    void isValid_noSpecial() {
        assertFalse(instance().isValid("Password1"));
    }

    @Test
    void isValid_short() {
        assertFalse(instance().isValid("Passwo1!"));
    }
}
