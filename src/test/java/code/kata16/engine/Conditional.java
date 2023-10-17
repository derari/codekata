package code.kata16.engine;

import code.kata16.OtherServices;
import com.fasterxml.jackson.annotation.*;

public record Conditional<T extends State>(
        @JsonAlias("if")
        @JsonProperty(required = true)
        Condition<T> condition,
        @JsonAlias("then")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        ProcessingRule<T> ifTrue,
        @JsonAlias("else")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        ProcessingRule<T> ifFalse
) implements ProcessingRule<T> {

    public Conditional(@JsonAlias("if")
                       @JsonProperty(required = true)
                       Condition<T> condition,
                       @JsonAlias("then")
                       ProcessingRule<T> ifTrue,
                       @JsonAlias("else")
                       ProcessingRule<T> ifFalse) {
        this.condition = condition;
        this.ifTrue = NoOp.nonNull(ifTrue);
        this.ifFalse = NoOp.nonNull(ifFalse);
    }

    @Override
    public boolean apply(T state, OtherServices services) {
        var test = condition().test(state, services);
        state.log("? %s: %s", test, condition());
        test = (test ? ifTrue() : ifFalse()).apply(state, services);
        state.log("/? %s", test);
        return test;
    }
}
