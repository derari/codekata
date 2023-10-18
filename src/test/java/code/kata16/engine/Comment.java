package code.kata16.engine;

import code.kata16.OtherServices;

public record Comment<T extends State>(Action<T> rule, String comment) implements Action<T> {

    @Override
    public boolean apply(T state, OtherServices services) {
        var result = rule().apply(state, services);
        if (result) state.log("  (" + comment() + ")");
        return result;
    }
}
