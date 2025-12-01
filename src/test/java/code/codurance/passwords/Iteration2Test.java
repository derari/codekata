package code.codurance.passwords;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class Iteration2Test extends PasswordRuleTestBase {

    @Override
    protected PasswordRule instance() {
        return IMPL;
    }

    static final PasswordRule IMPL = MatchAll.of(new MinLength(9))
            .and(CharacterMatch.HAS_UPPER)
            .and(CharacterMatch.HAS_LOWER)
            .and(CharacterMatch.HAS_NUMBER)
            .and(CharacterMatch.HAS_SPECIAL);

    static class MatchAll implements PasswordRule {

        static MatchAll of(PasswordRule... rules) {
            return new MatchAll(List.of(rules));
        }

        private final List<PasswordRule> rules;

        public MatchAll(List<PasswordRule> rules) {
            this.rules = rules;
        }

        @Override
        public boolean isValid(String password) {
            return rules.stream().allMatch(r -> r.isValid(password));
        }

        public MatchAll and(PasswordRule... more) {
            var newRules = new ArrayList<>(rules);
            newRules.addAll(List.of(more));
            return new MatchAll(newRules);
        }
    }

    static class CharacterMatch implements PasswordRule {

        static final CharacterMatch HAS_UPPER =  new CharacterMatch(Character::isUpperCase);
        static final CharacterMatch HAS_LOWER =  new CharacterMatch(Character::isLowerCase);
        static final CharacterMatch HAS_NUMBER =  new CharacterMatch(Character::isDigit);
        static final CharacterMatch HAS_SPECIAL =  new CharacterMatch(c -> !(
                        Character.isDigit(c)
                        || Character.isLowerCase(c)
                        || Character.isUpperCase(c)));

        private final Predicate<Character> predicate;

        public CharacterMatch(Predicate<Character> predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean isValid(String password) {
            for  (var i = 0; i < password.length(); i++) {
                if (predicate.test(password.charAt(i))) {
                    return true;
                }
            }
            return false;
        }
    }

    static class MinLength  implements PasswordRule {

        private final int  minLength;

        public MinLength(int minLength) {
            this.minLength = minLength;
        }

        @Override
        public boolean isValid(String password) {
            return password.length() >= minLength;
        }
    }
}
