package code.kata16.engine;

import code.kata16.ProductClass;
import code.kata16.ProductType;

import java.util.List;

public class ConditionBuilder {

    private Type type = Type.MISSING;
    private Condition<PaymentProcessingState> condition = new And<>();

    public ConditionBuilder() {
    }

    private ConditionBuilder setType(Type type) {
        if (this.type != Type.MISSING && this.type != type) {
            throw new IllegalArgumentException(this.type + " != " + type);
        }
        this.type = type;
        return this;
    }

    public ConditionBuilder withAll(List<Condition<PaymentProcessingState>> conditions) {
        this.condition = this.condition.and(And.all(conditions));
        return setType(Type.AND);
    }

    public ConditionBuilder withProductClass(ProductClass clazz) {
        this.condition = this.condition.and(new IsProductClass(clazz));
        return setType(Type.AND);
    }

    public ConditionBuilder withProductType(ProductType type) {
        this.condition = this.condition.and(new IsProductType(type));
        return setType(Type.AND);
    }

    public ConditionBuilder withProductName(String name) {
        this.condition = this.condition.and(new HasProductName(name));
        return setType(Type.AND);
    }

    public Condition<?> build() {
            return type.build(this);
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
}
