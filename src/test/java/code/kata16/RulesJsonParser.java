package code.kata16;

import code.kata16.engine.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RulesJsonParser {

    private final ObjectMapper om = new ObjectMapper();

    public RulesJsonParser() {
        om.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        om.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        om.addMixIn(ProcessingRule.class, RuleMixin.class);
        om.addMixIn(Condition.class, ConditionMixin.class);
    }

    public ProcessingRule<PaymentProcessingState> parse(String input) {
        try {
            return Sequence.of(om.readValue(input, T_RULES));
        } catch (RuntimeException | IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static final TypeReference<List<ProcessingRule<PaymentProcessingState>>> T_RULES = new TypeReference<>() { };

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "action", defaultImpl = UseRuleBuilder.class)
    @JsonTypeIdResolver(Types.class)
    private interface RuleMixin {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "test", defaultImpl = UseConditionBuilder.class)
    @JsonTypeIdResolver(Types.class)
    private interface ConditionMixin {
    }

    @JsonDeserialize(builder = RuleBuilder.class)
    private interface UseRuleBuilder extends ProcessingRule<State> {
    }

    @JsonDeserialize(builder = ConditionBuilder.class)
    private interface UseConditionBuilder extends Condition<State> {
    }

    static class Types implements TypeIdResolver {

        private static final Map<String, JavaType> TYPES = new ConcurrentHashMap<>();

        @Override
        public void init(JavaType baseType) {
        }

        @Override
        public String idFromValue(Object value) {
            throw new UnsupportedOperationException("idFromValue: " + value);
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            throw new UnsupportedOperationException("idFromValueAndType: " + value + " " + suggestedType);
        }

        @Override
        public String idFromBaseType() {
            throw new UnsupportedOperationException("idFromBaseType");
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) {
            return TYPES.computeIfAbsent(id, t -> {
                try {
                    var pkg = RulesJsonParser.class.getPackageName();
                    var clazz = Class.forName(pkg + ".engine." + id);
                    return TypeFactory.defaultInstance().constructType(clazz);
                } catch (RuntimeException | ClassNotFoundException ex) {
                    throw new IllegalArgumentException("id", ex);
                }
            });
        }

        @Override
        public String getDescForKnownTypeIds() {
            throw new UnsupportedOperationException("getDescForKnownTypeIds");
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            throw new UnsupportedOperationException("getMechanism");
        }
    }
}
