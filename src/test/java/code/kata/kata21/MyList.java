package code.kata.kata21;

import java.util.function.IntFunction;

public interface MyList<E> {

    void add(E e);

    Node<E> find(E element);

    void remove(Node<E> node);

    E[] toArray(IntFunction<E[]> generator);

    interface Node<E> {

        E value();
    }
}
