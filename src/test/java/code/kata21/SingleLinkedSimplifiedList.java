package code.kata21;

import java.util.function.IntFunction;

public class SingleLinkedSimplifiedList<E> implements MyList<E> {

    private final SLNode<E> head = new SLNode<>();
    private SLNode<E> tail = new SLNode<>();
    private int size = 0;

    {
        head.next = tail;
    }

    @Override
    public void add(E e) {
        tail.value = e;
        tail.next = new SLNode<>();
        tail = tail.next;
        size++;
    }

    @Override
    public Node<E> find(E element) {
        var current = head.next;
        while (current != tail) {
            if (element.equals(current.value)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public void remove(Node<E> node) {
        var current = head;
        while (current != null) {
            if (current.next == node) {
                current.next = current.next.next;
                size--;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public E[] toArray(IntFunction<E[]> generator) {
        var result = generator.apply(size);
        var current = head;
        for (int i = 0; i < size; i++) {
            current = current.next;
            result[i] = current.value;
        }
        return result;
    }

    private static class SLNode<E> implements Node<E> {

        E value = null;
        SLNode<E> next = null;

        @Override
        public E value() {
            return value;
        }
    }
}
