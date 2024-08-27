package code.kata21;

public class SparseArrayListTest extends MyListTestBase {

    @Override
    MyList<String> newList() {
        return new SparseArrayList<>();
    }

    static class Impl2Test extends MyListTestBase {
        @Override
        MyList<String> newList() {
            return new SparseArrayList<>() {
                @Override
                protected void grow() {
                    grow2();
                }
            };
        }
    }

    static class Impl3Test extends MyListTestBase {
        @Override
        MyList<String> newList() {
            return new SparseArrayList<>() {
                @Override
                protected void grow() {
                    grow3();
                }
            };
        }
    }
}
