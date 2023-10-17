package code.kata16.engine;

import code.kata16.Department;

import java.util.List;

public class RuleBuilder {

    private Type type = Type.NOOP;
    private Condition<PaymentProcessingState> condition;
    private ProcessingRule<PaymentProcessingState> rule1;
    private ProcessingRule<PaymentProcessingState> rule2;

    public RuleBuilder() {
    }

    private RuleBuilder requireType(Type type) {
        if (this.type != Type.NOOP && this.type != type) {
            throw new IllegalArgumentException(this.type + " != " + type);
        }
        if (this.type == Type.SINGLE) {
            throw new IllegalArgumentException("already set");
        }
        this.type = type;
        return this;
    }

    public RuleBuilder withSteps(List<ProcessingRule<PaymentProcessingState>> rules) {
        return with1(Type.SINGLE, rules);
    }

    public RuleBuilder withIf(List<Condition<PaymentProcessingState>> conditions) {
        this.condition = And.all(conditions);
        return requireType(Type.CONDITIONAL);
    }

    public RuleBuilder withThen(List<ProcessingRule<PaymentProcessingState>> rules) {
        return with1(Type.CONDITIONAL, rules);
    }

    public RuleBuilder withElse(List<ProcessingRule<PaymentProcessingState>> rules) {
        return with2(Type.CONDITIONAL, rules);
    }

    public RuleBuilder withPackagingSlipFor(Department department) {
        return with1(Type.SINGLE, List.of(new GeneratePackingSlip(department)));
    }

    private RuleBuilder with1(Type type, List<ProcessingRule<PaymentProcessingState>> rules) {
        rule1 = Sequence.of(rules);
        return requireType(type);
    }

    private RuleBuilder with2(Type type, List<ProcessingRule<PaymentProcessingState>> rules) {
        rule2 = Sequence.of(rules);
        return requireType(type);
    }

    public ProcessingRule<?> build() {
        return type.build(this);
    }

    private enum Type {

        NOOP {
            @Override
            ProcessingRule<?> build(RuleBuilder builder) {
                return new NoOp<>(false);
            }
        },
        CONDITIONAL {
            @Override
            ProcessingRule<?> build(RuleBuilder builder) {
                return new Conditional<>(builder.condition, builder.rule1, builder.rule2);
            }
        },
        SINGLE {
            @Override
            ProcessingRule<?> build(RuleBuilder builder) {
                return builder.rule1;
            }
        };

        abstract ProcessingRule<?> build(RuleBuilder builder);
    }
}
