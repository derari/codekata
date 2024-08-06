package code.kata21;

import java.util.function.IntFunction;

public class SingleLinkedList<E> implements MyList<E> {

    private final SLNode<E> head = new SLNode<>(null);
    private SLNode<E> tail = head;
    private int size;

    @Override
    public void add(E e) {
        tail.next = new SLNode<>(e);
        tail = tail.next;
        size++;
    }

    @Override
    public Node<E> find(E element) {
        var current = head.next;
        while (current != null) {
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
                if (current.next == null) {
                    tail = current;
                }
                size--;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public E[] toArray(IntFunction<E[]> generator) {
        var result = generator.apply(size);
        var current = head.next;
        for (int i = 0; i < size; i++) {
            result[i] = current.value;
            current = current.next;
        }
        return result;
    }

    private static class SLNode<E> implements MyList.Node<E> {

        final E value;
        SLNode<E> next = null;

        public SLNode(E value) {
            this.value = value;
        }

        @Override
        public E value() {
            return value;
        }
    }
}
