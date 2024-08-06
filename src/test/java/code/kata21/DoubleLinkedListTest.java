package code.kata21;

public class DoubleLinkedListTest extends MyListTestBase {

    @Override
    MyList<String> newList() {
        return new DoubleLinkedList<>();
    }
}
