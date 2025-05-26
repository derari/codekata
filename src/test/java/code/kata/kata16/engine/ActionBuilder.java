package code.kata.kata16.engine;

import code.kata.kata16.*;
import code.kata.kata17.OrderWorkflowKey;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.*;
import java.util.stream.Stream;

@JsonPOJOBuilder(withPrefix = "", buildMethodName = "wrapped")
public class ActionBuilder {

    private Type type = Type.NOOP;
    private final List<Action<OrderProcessingState>> main = new ArrayList<>();
    private Condition<OrderProcessingState> condition;
    private Action<OrderProcessingState> orElse;
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

    public ActionBuilder steps(List<Action<OrderProcessingState>> rules) {
        return main(Type.SINGLE, rules);
    }

    @JsonProperty("if")
    public ActionBuilder when(List<Condition<OrderProcessingState>> conditions) {
        requireType(Type.CONDITIONAL);
        this.condition = And.all(conditions);
        return this;
    }

    @JsonProperty("then")
    public ActionBuilder ifTrue(List<Action<OrderProcessingState>> rules) {
        return main(Type.CONDITIONAL, rules);
    }

    @JsonProperty("else")
    public ActionBuilder ifFalse(List<Action<OrderProcessingState>> rules) {
        requireType(Type.CONDITIONAL);
        orElse = Sequence.of(rules);
        return this;
    }

    public ActionBuilder packagingSlipFor(List<Department> departments) {
        var actions = departments.stream().map(GeneratePackingSlip::new);
        return next(actions);
    }

    public ActionBuilder shipExtraItem(List<ShipExtraItem> extraItem) {
        return next(extraItem);
    }

    public ActionBuilder schedule(List<OrderWorkflowKey> workflows) {
        var actions = workflows.stream().map(ScheduleWorkflow::new).toList();
        return next(actions);
    }

    public ActionBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    @JsonIgnore
    private ActionBuilder next(Collection<? extends Action<OrderProcessingState>> rules) {
        return next(rules.stream());
    }

    private ActionBuilder next(Stream<? extends Action<OrderProcessingState>> rules) {
        return main(Type.SEQUENCE, rules);
    }

    private ActionBuilder main(Type type, Collection<? extends Action<OrderProcessingState>> rules) {
        return main(type, rules.stream());
    }

    private ActionBuilder main(Type type, Stream<? extends Action<OrderProcessingState>> rules) {
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

    protected Action<OrderProcessingState> main() {
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
        static Action<OrderProcessingState> get(String name) {
            try {
                return (Action<OrderProcessingState>) RulesParser.getImplementation(name).getConstructor().newInstance();
            } catch (ReflectiveOperationException ex) {
                throw new IllegalArgumentException(name, ex);
            }
        }

        Action<OrderProcessingState> get();
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
