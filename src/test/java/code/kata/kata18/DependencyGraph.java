package code.kata.kata18;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

public class DependencyGraph<T> {

    private final Map<T, Set<T>> map = new HashMap<>();
    private Tracer<? super T> tracer = NOOP;

    public void setTracer(Tracer<? super T> tracer) {
        this.tracer = tracer;
    }

    @SafeVarargs
    public final void add(T key, T... dependencies) {
        add(key, Arrays.asList(dependencies));
    }

    public void add(T key, Collection<T> dependencies) {
        if (streamAll(dependencies).anyMatch(key::equals)) {
            throw new IllegalArgumentException("circular dependencies");
        }

        map.computeIfAbsent(key, n -> new HashSet<>()).addAll(dependencies);
    }

    private Set<T> getDirect(T key) {
        return map.getOrDefault(key, Set.of());
    }

    public Set<T> getAll(T key) {
        return getAll(List.of(key));
    }

    public Set<T> getAll(Collection<T> keys) {
        return streamAll(keys)
                .collect(Collectors.toSet());
    }

    public Set<T> getAll_boring(Collection<T> keys) {
        var bag = new HashSet<T>();
        keys.forEach(k -> collectAll(k, bag));
        return bag;
    }

    private void collectAll(T key, Set<T> bag) {
        tracer.beforeCollect(key);
        getDirect(key).stream()
                .filter(bag::add)
                .forEach(d -> collectAll(d, bag));
        tracer.afterCollect(key);
    }

    public Stream<T> streamAll(T key) {
        return streamAll(List.of(key));
    }

    public Stream<T> streamAll_broken(T key) {
        return streamAll_broken(Stream.of(key), new HashSet<>());
    }

    private Stream<T> streamAll_broken(Stream<T> keys, Set<T> distinct) {
        return keys
                .peek(k -> tracer.beforeCollect(k))
                .flatMap(k -> getDirect(k).stream()
                    .filter(distinct::add)
                    .flatMap(d -> Stream.concat(
                            Stream.of(d),
                            streamAll_broken(Stream.of(d), distinct)
                    )));
    }

    public Stream<T> streamAll(Collection<T> keys) {
        return StreamSupport.stream(spliterator(keys), false);
    }

    public Stream<T> streamAll2(Collection<T> keys) {
        var split =  Spliterators.spliteratorUnknownSize(iterator(keys),
                Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.CONCURRENT | Spliterator.NONNULL);
        return StreamSupport.stream(split, false);
    }

    private Iterator<T> iterator(Collection<T> keys) {
        var distinct = new HashSet<>(keys);
        var keyQueue = new ArrayDeque<>(keys);
        var valueQueue = new ArrayDeque<T>();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                while (valueQueue.isEmpty()) {
                    var key = keyQueue.pollFirst();
                    if (key == null) return false;
                    enqueueDependencies(key, distinct, keyQueue, valueQueue);
                }
                return true;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return valueQueue.removeFirst();
            }
        };
    }

    private Spliterator<T> spliterator(Collection<T> keys) {
        var distinct = new HashSet<>(keys);
        var keyQueue = new ArrayDeque<>(keys);
        return new Spliterator<>() {
            ArrayDeque<T> valueQueue = new ArrayDeque<>();

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                T next;
                synchronized (this) {
                    if (isEmpty()) return false;
                    next = valueQueue.removeFirst();
                }
                action.accept(next);
                return true;
            }

            private boolean isEmpty() {
                while (valueQueue.isEmpty()) {
                    if (!pollInto(valueQueue)) return true;
                }
                return false;
            }

            private boolean pollInto(Collection<T> target) {
                var key = keyQueue.pollFirst();
                if (key == null) return false;
                enqueueDependencies(key, distinct, keyQueue, target);
                return true;
            }

            @Override
            public synchronized Spliterator<T> trySplit() {
                if (isEmpty()) return null;
                var values = valueQueue;
                valueQueue = new ArrayDeque<>();
                splitMore(values);
                return values.spliterator();
            }

            @SuppressWarnings("StatementWithEmptyBody")
            private void splitMore(Collection<T> split) {
                var splitSize = Math.min(512, Math.max(16, keyQueue.size() / 2));
                do { } while (split.size() < splitSize && pollInto(split));
            }

            @Override
            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            @Override
            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.CONCURRENT | Spliterator.NONNULL;
            }
        };
    }

    private void enqueueDependencies(T key, Set<T> distinct, Collection<T> sink1, Collection<T> sink2) {
        enqueueDependencies(key, distinct, v -> {
            sink1.add(v);
            sink2.add(v);
        });
    }

    private void enqueueDependencies(T key, Set<T> distinct, Consumer<T> sink) {
        tracer.beforeCollect(key);
        getDirect(key).stream()
                .filter(distinct::add)
                .forEach(sink);
        tracer.afterCollect(key);
    }

    private static final Tracer<Object> NOOP = new Tracer<>() {
        @Override
        public void beforeCollect(Object key) {
        }
        @Override
        public void afterCollect(Object key) {
        }
    };

    public interface Tracer<T> {

        void beforeCollect(T key);

        void afterCollect(T key);
    }
}
