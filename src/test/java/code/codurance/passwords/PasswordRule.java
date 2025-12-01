package code.codurance.passwords;

import java.util.function.Consumer;

public interface PasswordRule {

    boolean isValid(String password);

    default boolean isValid(String password, Consumer<String> errorLog) {
        if (isValid(password)) {
            return true;
        }
        errorLog.accept("Password failed rule: " + this);
        return false;
    }
}
