package code.kata.kata11;

import java.util.*;
import java.util.function.IntConsumer;
import java.util.stream.*;

public class BucketSort implements Iterable<Integer> {

    private final int min;
    private final int[] buckets;

    public BucketSort(int size) {
        this.min = 0;
        this.buckets = new int[size];
    }

    public BucketSort(int min, int max) {
        this.min = min;
        this.buckets = new int[max - min + 1];
    }

    public void add(int i) {
        buckets[i - min]++;
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            int next = -1;
            int remaining = 0;

            @Override
            public boolean hasNext() {
                while (remaining == 0 && ++next < buckets.length) {
                    remaining = buckets[next];
                }
                return remaining > 0;
            }

            @Override
            public int nextInt() {
                if (!hasNext()) throw new NoSuchElementException();
                remaining--;
                return next + min;
            }
        };
    }

    public IntStream stream() {
        var spl = Spliterators.spliteratorUnknownSize(iterator(), Spliterator.SORTED | Spliterator.NONNULL);
        return StreamSupport.intStream(spl, false);
    }
}
