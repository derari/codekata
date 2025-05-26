package code.kata.kata16;

import code.kata.kata16.engine.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RulesParser {

    private final ObjectMapper json = new ObjectMapper();
    private final ObjectMapper yaml;

    public RulesParser() {
        json.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        json.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        json.addMixIn(Action.class, ActionMixin.class);
        json.addMixIn(Condition.class, ConditionMixin.class);
        yaml = json.copyWith(new YAMLFactory());
    }

    public Action<OrderProcessingState> parseJson(String input) {
        try {
            return Sequence.of(json.readValue(input, T_ACTIONS));
        } catch (RuntimeException | IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public Action<OrderProcessingState> parseYaml(String input) {
        try {
            return Sequence.of(yaml.readValue(input, T_ACTIONS));
        } catch (RuntimeException | IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static Class<?> getImplementation(String name) {
        try {
            var pkg = RulesParser.class.getPackageName();
            return Class.forName(pkg + ".engine." + name);
        } catch (RuntimeException | ClassNotFoundException ex) {
            throw new IllegalArgumentException(name, ex);
        }
    }

    private static final TypeReference<List<Action<OrderProcessingState>>> T_ACTIONS = new TypeReference<>() { };

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "action", defaultImpl = UseActionDeserializer.class)
    @JsonTypeIdResolver(Types.class)
    private interface ActionMixin {
    }

    @JsonDeserialize(using = ActionDeserializer.class)
    private interface UseActionDeserializer extends Action<State> {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "test", defaultImpl = UseConditionDeserializer.class)
    @JsonTypeIdResolver(Types.class)
    private interface ConditionMixin {
    }

    @JsonDeserialize(using = ConditionDeserializer.class)
    private interface UseConditionDeserializer extends Condition<State> {
    }

    private static class Types implements TypeIdResolver {

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
            return TYPES.computeIfAbsent(id, name -> TypeFactory.defaultInstance().constructType(getImplementation(name)));
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

    private static class ActionDeserializer extends JsonDeserializer<Action<?>> {

        @Override
        public Action<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.currentToken() == JsonToken.VALUE_STRING) {
                return p.getCodec().readValue(p, ActionBuilder.Atomic.class).get();
            }
            return p.getCodec().readValue(p, ActionBuilder.Result.class).action();
        }
    }

    private static class ConditionDeserializer extends JsonDeserializer<Condition<?>> {

        @Override
        public Condition<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.currentToken() == JsonToken.VALUE_STRING) {
                return p.getCodec().readValue(p, ConditionBuilder.Atomic.class).get();
            }
            return p.getCodec().readValue(p, ConditionBuilder.Result.class).condition();
        }
    }
}
