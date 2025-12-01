package code.codurance.passwords;

class Iteration1Test extends PasswordRuleTestBase {

    @Override
    protected PasswordRule instance() {
        return new Impl();
    }

    static class Impl implements PasswordRule {

        @Override
        public boolean isValid(String password) {
            if (password.length() < 9) return false;
            var hasUpper = false;
            var hasLower = false;
            var hasDigit = false;
            var hasSpecial = false;
            for (var i = 0; i < password.length(); i++) {
                if (Character.isUpperCase(password.charAt(i))) {
                    hasUpper = true;
                    continue;
                }
                if (Character.isLowerCase(password.charAt(i))) {
                    hasLower = true;
                    continue;
                }
                if (Character.isDigit(password.charAt(i))) {
                    hasDigit = true;
                    continue;
                }
                hasSpecial = true;
            }
            return hasUpper && hasLower && hasDigit && hasSpecial;
        }
    }
}
