package code.kata21;

import java.util.function.IntFunction;

public class DoubleLinkedList<E> implements MyList<E> {

    private final DLNode<E> head = new DLNode<>(null);
    private final DLNode<E> tail = new DLNode<>(null);
    private int size = 0;

    {
        head.setNext(tail);
    }

    @Override
    public void add(E e) {
        var last = new DLNode<>(e);
        tail.prev.setNext(last);
        last.setNext(tail);
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
        var n = (DLNode<E>) node;
        if (n.prev.next != n) return;
        n.prev.setNext(n.next);
        size--;
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

    private static class DLNode<E> implements Node<E> {

        E value;
        DLNode<E> prev = null;
        DLNode<E> next = null;

        DLNode(E value) {
            this.value = value;
        }

        @Override
        public E value() {
            return value;
        }

        void setNext(DLNode<E> other) {
            next = other;
            other.prev = this;
        }
    }
}
