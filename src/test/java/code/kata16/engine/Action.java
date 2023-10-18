package code.kata16.engine;

import code.kata16.OtherServices;

public interface Action<T extends State> {

    boolean apply(T state, OtherServices services);
}
