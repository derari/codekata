package code.kata.kata16.engine;

import code.kata.kata16.OtherServices;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonDeserialize(using = Or.OrDeserializer.class)
public record Or<T extends State>(List<Condition<T>> conditions) implements Condition<T> {

    public static <T extends State> Condition<T> any(List<? extends Condition<T>> conditions) {
        if (conditions == null) return (a, b) -> false;
        List<Condition<T>> filtered = conditions.stream()
                .filter(Objects::nonNull)
                .flatMap(c -> c instanceof Or<T> or ? or.conditions().stream() : Stream.of(c))
                .toList();
        if (filtered.size() == 1) return filtered.getFirst();
        return new Or<>(filtered);
    }

    @SafeVarargs
    public Or(Condition<T>... conditions) {
        this(List.of(conditions));
    }

    @Override
    public boolean test(T state, OtherServices services) {
        return conditions().stream().anyMatch(c -> c.test(state, services));
    }

    static class OrDeserializer extends JsonDeserializer<Or<?>> {

        private static final TypeReference<List<Condition<?>>> T_CONDITIONS = new TypeReference<>() { };

        @Override
        public Or<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Or<>(new ArrayList<>(p.readValueAs(T_CONDITIONS)));
        }
    }
}
