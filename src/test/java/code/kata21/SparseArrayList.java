package code.kata21;

import java.util.Arrays;
import java.util.function.IntFunction;

public class SparseArrayList<E> implements MyList<E> {

    private SANode<E>[] array = new SANode[1];
    private int next = 0;
    private int size = 0;

    @Override
    public void add(E e) {
        if (next == array.length) {
            compressOrGrow();
        }
        array[next] = new SANode<>(e, next);
        next++;
        size++;
    }

    private void compressOrGrow() {
        if (size == array.length) {
            array = Arrays.copyOf(array, size * 2);
            return;
        }
        grow();
        next = size;
    }

    @Override
    public Node<E> find(E element) {
        for (var node: array) {
            if (node != null && element.equals(node.value)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public void remove(Node<E> node) {
        if (node instanceof SANode<E> n
                && array.length > n.index
                && array[n.index] == n) {
            array[n.index] = null;
            size--;
        }
    }

    @Override
    public E[] toArray(IntFunction<E[]> generator) {
        var result = generator.apply(size);
        int a = 0;
        for (int i = 0; a < size; i++) {
            if (array[i] != null) {
                result[a] = array[i].value;
                a++;
            }
        }
        return result;
    }

    protected void grow() {
        grow1();
    }

    protected void grow1() {
        var a = 0;
        while (array[a] != null) a++;
        var b = a + 1;
        while (a < size) {
            if (array[b] != null) {
                array[a] = array[b];
                array[a].index = a;
                if (b >= size) array[b] = null;
                a++;
            }
            b++;
        }
    }

    protected void grow2() {
        var a = 0;
        while (array[a] != null) a++;
        var b = a + 1;
        while (a < size) {
            if (array[b] != null) {
                array[a] = array[b];
                array[a].index = a;
                array[b] = null;
                a++;
            }
            b++;
        }
    }

    protected void grow3() {
        var a = 0;
        var b = 0;
        while (a < size) {
            if (array[b] != null) {
                array[a] = array[b];
                array[a].index = a;
                if (b >= size) array[b] = null;
                a++;
            }
            b++;
        }
    }

    private static class SANode<E> implements Node<E> {

        final E value;
        int index;

        SANode(E value, int index) {
            this.value = value;
            this.index = index;
        }

        @Override
        public E value() {
            return value;
        }
    }
}
