package code.codurance.passwords;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class Iteration3Test extends PasswordRuleTestBase {

    private List<String> log = new ArrayList<>();

    @Override
    protected PasswordRule instance() {
        return IMPL;
    }

    @Test
    void isValid_withLog() {
        var result = IMPL.isValid("password", log::add);
        assertFalse(result);
        assertEquals(4, log.size());
        assertTrue(log.contains("Password must be at least 9 characters long"));
        assertTrue(log.contains("Password must contain at least one uppercase letter"));
        assertTrue(log.contains("Password must contain at least one digit"));
        assertTrue(log.contains("Password must contain at least one special character"));
    }

    static final PasswordRule IMPL = MatchAll.of(new MinLength(9))
            .and(CharacterMatch.HAS_UPPER)
            .and(CharacterMatch.HAS_LOWER)
            .and(CharacterMatch.HAS_NUMBER)
            .and(CharacterMatch.HAS_SPECIAL);

    abstract static class LoggingPasswordRule implements PasswordRule {

        @Override
        public boolean isValid(String password) {
            return isValid(password, err -> {});
        }

        @Override
        public abstract boolean isValid(String password, Consumer<String> errorLog);
    }

    static class MatchAll extends LoggingPasswordRule {

        static MatchAll of(PasswordRule... rules) {
            return new MatchAll(List.of(rules));
        }

        private final List<PasswordRule> rules;

        public MatchAll(List<PasswordRule> rules) {
            this.rules = rules;
        }

        @Override
        public boolean isValid(String password, Consumer<String> errorLog) {
            var ok = true;
            for (var r : rules) {
                if (!r.isValid(password, errorLog)) {
                    ok = false;
                }
            }
            return ok;
        }

        public MatchAll and(PasswordRule... more) {
            var newRules = new ArrayList<>(rules);
            newRules.addAll(List.of(more));
            return new MatchAll(newRules);
        }
    }

    static class CharacterMatch extends LoggingPasswordRule {

        static final CharacterMatch HAS_UPPER =  new CharacterMatch( "uppercase letter", Character::isUpperCase);
        static final CharacterMatch HAS_LOWER =  new CharacterMatch("lowercase letter", Character::isLowerCase);
        static final CharacterMatch HAS_NUMBER =  new CharacterMatch("digit", Character::isDigit);
        static final CharacterMatch HAS_SPECIAL =  new CharacterMatch("special character", c -> !(
                        Character.isDigit(c)
                        || Character.isLowerCase(c)
                        || Character.isUpperCase(c)));

        private final String title;
        private final Predicate<Character> predicate;

        public CharacterMatch(String title, Predicate<Character> predicate) {
            this.title = title;
            this.predicate = predicate;
        }

        @Override
        public boolean isValid(String password, Consumer<String> errorLog) {
            for  (var i = 0; i < password.length(); i++) {
                if (predicate.test(password.charAt(i))) {
                    return true;
                }
            }
            errorLog.accept("Password must contain at least one " + title);
            return false;
        }
    }

    static class MinLength extends LoggingPasswordRule {

        private final int  minLength;

        public MinLength(int minLength) {
            this.minLength = minLength;
        }

        @Override
        public boolean isValid(String password, Consumer<String> errorLog) {
            if (password.length() >= minLength) {
                return true;
            }
            errorLog.accept("Password must be at least " + minLength + " characters long");
            return false;
        }
    }
}
