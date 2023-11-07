package code.kata16.engine;

import code.kata16.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

@JsonPOJOBuilder(withPrefix = "", buildMethodName = "wrapped")
public class ConditionBuilder {

    private Type type = Type.MISSING;
    private Condition<PaymentProcessingState> condition = new And<>();

    public ConditionBuilder() {
    }

    private void setType(Type type) {
        if (this.type != Type.MISSING && this.type != type) {
            throw new IllegalArgumentException(this.type + " != " + type);
        }
        this.type = type;
    }

    public ConditionBuilder all(List<Condition<PaymentProcessingState>> conditions) {
        return and(And.all(conditions));
    }

    public ConditionBuilder productType(ProductType type) {
        return and(new IsProductType(type));
    }

    public ConditionBuilder productName(String name) {
        return and(new HasProductName(name));
    }

    public ConditionBuilder hasUpgradableMembership(boolean expected) {
        return and(new HasUpgradableMembership().is(expected));
    }

    public ConditionBuilder and(Condition<PaymentProcessingState> condition) {
        setType(Type.AND);
        this.condition = this.condition.and(condition);
        return this;
    }

    public Condition<?> build() {
            return type.build(this);
        }

    public Result<?> wrapped() {
        return new Result<>(build());
    }

    private enum Type {

        MISSING {
            @Override
            Condition<?> build(ConditionBuilder builder) {
                throw new IllegalArgumentException("condition required");
            }
        },
        AND {
            @Override
            Condition<?> build(ConditionBuilder builder) {
                return builder.condition;
            }
        };

        abstract Condition<?> build(ConditionBuilder builder);
    }

    public interface Atomic {

        @JsonCreator
        static Atomic of(String name) {
            return () -> get(name);
        }

        @SuppressWarnings("unchecked")
        static Condition<PaymentProcessingState> get(String name) {
            try {
                return (Condition<PaymentProcessingState>) RulesParser.getImplementation(name).getConstructor().newInstance();
            } catch (ReflectiveOperationException ex) {
                throw new IllegalArgumentException(name, ex);
            }
        }

        Condition<PaymentProcessingState> get();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @JsonDeserialize(builder = ConditionBuilder.class)
    public record Result<T extends State>(Condition<T> condition) implements Condition<T> {

        @Override
        public boolean test(T state, OtherServices services) {
            return condition().test(state, services);
        }
    }
}
