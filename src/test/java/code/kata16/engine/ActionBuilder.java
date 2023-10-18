package code.kata16.engine;

import code.kata16.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.*;
import java.util.stream.Stream;

@JsonPOJOBuilder(withPrefix = "", buildMethodName = "wrapped")
public class ActionBuilder {

    private Type type = Type.NOOP;
    private final List<Action<PaymentProcessingState>> main = new ArrayList<>();
    private Condition<PaymentProcessingState> condition;
    private Action<PaymentProcessingState> orElse;
    private String comment;

    public ActionBuilder() {
    }

    private void requireType(Type type) {
        if (this.type != Type.NOOP && this.type != type) {
            throw new IllegalArgumentException(this.type + " != " + type);
        }
        if (this.type == Type.SINGLE) {
            throw new IllegalArgumentException("already set");
        }
        this.type = type;
    }

    public ActionBuilder steps(List<Action<PaymentProcessingState>> rules) {
        return main(Type.SINGLE, rules);
    }

    @JsonProperty("if")
    public ActionBuilder when(List<Condition<PaymentProcessingState>> conditions) {
        requireType(Type.CONDITIONAL);
        this.condition = And.all(conditions);
        return this;
    }

    @JsonProperty("then")
    public ActionBuilder ifTrue(List<Action<PaymentProcessingState>> rules) {
        return main(Type.CONDITIONAL, rules);
    }

    @JsonProperty("else")
    public ActionBuilder ifFalse(List<Action<PaymentProcessingState>> rules) {
        requireType(Type.CONDITIONAL);
        orElse = Sequence.of(rules);
        return this;
    }

    public ActionBuilder packagingSlipFor(List<Department> departments) {
        var actions = departments.stream().map(GeneratePackingSlip::new);
        return main(Type.SEQUENCE, actions);
    }

    public ActionBuilder shipExtraItem(List<ShipExtraItem> extraItem) {
        return main(Type.SEQUENCE, extraItem);
    }

    public ActionBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    private ActionBuilder main(Type type, Collection<? extends Action<PaymentProcessingState>> rules) {
        return main(type, rules.stream());
    }

    private ActionBuilder main(Type type, Stream<? extends Action<PaymentProcessingState>> rules) {
        requireType(type);
        rules.forEach(main::add);
        return this;
    }

    public Action<?> build() {
        var result = type.build(this);
        if (comment == null || comment.isBlank()) return result;
        return new Comment<>(result, comment);
    }

    public Result<?> wrapped() {
        return new Result<>(build());
    }

    protected Action<PaymentProcessingState> main() {
        return Sequence.of(main);
    }

    private enum Type {

        NOOP {
            @Override
            Action<?> build(ActionBuilder builder) {
                return NoOp.NONE;
            }
        },
        CONDITIONAL {
            @Override
            Action<?> build(ActionBuilder builder) {
                return new Conditional<>(builder.condition, builder.main(), builder.orElse);
            }
        },
        SINGLE {
            @Override
            Action<?> build(ActionBuilder builder) {
                return builder.main();
            }
        },
        SEQUENCE {
            @Override
            Action<?> build(ActionBuilder builder) {
                return builder.main();
            }
        };

        abstract Action<?> build(ActionBuilder builder);
    }

    public interface Atomic {

        @JsonCreator
        static Atomic of(String name) {
            return () -> get(name);
        }

        @SuppressWarnings("unchecked")
        static Action<PaymentProcessingState> get(String name) {
            try {
                return (Action<PaymentProcessingState>) RulesParser.getImplementation(name).getConstructor().newInstance();
            } catch (ReflectiveOperationException ex) {
                throw new IllegalArgumentException(name, ex);
            }
        }

        Action<PaymentProcessingState> get();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @JsonDeserialize(builder = ActionBuilder.class)
    public record Result<T extends State>(Action<T> action) implements Action<T> {

        @Override
        public boolean apply(T state, OtherServices services) {
            return action().apply(state, services);
        }
    }
}
