package code.kata21;

public class SingleLinkedListTest extends MyListTestBase {

    @Override
    MyList<String> newList() {
        return new SingleLinkedList<>();
    }
}
