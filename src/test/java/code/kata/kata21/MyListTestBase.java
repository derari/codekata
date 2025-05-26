package code.kata.kata21;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class MyListTestBase {

    abstract MyList<String> newList();

    List<String> all(MyList<String> list) {
        return Arrays.asList(list.toArray(String[]::new));
    }

    MyList<String> initialList() {
        var list = newList();
        list.add("alice");
        list.add("bob");
        list.add("carol");
        return list;
    }

    @Test
    void toArray() {
        var list = initialList();

        assertEquals(List.of("alice", "bob", "carol"), all(list));
    }

    @Test
    void find() {
        var list = initialList();

        assertEquals("alice", list.find("alice").value());
        assertEquals("bob", list.find("bob").value());
        assertEquals("carol", list.find("carol").value());
        assertNull(list.find("dave"));
    }

    @Test
    void delete_1() {
        var list = initialList();

        list.remove(list.find("alice"));

        assertEquals(List.of("bob", "carol"), all(list));
    }

    @Test
    void delete_2() {
        var list = initialList();

        list.remove(list.find("bob"));

        assertEquals(List.of("alice", "carol"), all(list));
    }

    @Test
    void delete_3() {
        var list = initialList();

        list.remove(list.find("carol"));

        assertEquals(List.of("alice", "bob"), all(list));
    }

    @Test
    void delete_again() {
        var list = initialList();

        var bob = list.find("bob");
        list.remove(bob);
        list.remove(bob);

        assertEquals(List.of("alice", "carol"), all(list));

        list.add("dave");
        list.remove(bob);

        assertEquals(List.of("alice", "carol", "dave"), all(list));
    }

    @Test
    void example1() {
        var list = newList();

        assertNull(list.find("fred"));
        list.add("fred");
        assertEquals("fred", list.find("fred").value());

        assertNull(list.find("wilma"));
        list.add("wilma");
        assertEquals("fred", list.find("fred").value());
        assertEquals("wilma", list.find("wilma").value());
        assertEquals(List.of("fred", "wilma"), all(list));
    }

    @Test
    void example2() {
        var list = newList();
        list.add("fred");
        list.add("wilma");
        list.add("betty");
        list.add("barney");

        assertEquals(List.of("fred", "wilma", "betty", "barney"), all(list));
        list.remove(list.find("wilma"));
        assertEquals(List.of("fred", "betty", "barney"), all(list));
        list.remove(list.find("barney"));
        assertEquals(List.of("fred", "betty"), all(list));
        list.remove(list.find("fred"));
        assertEquals(List.of("betty"), all(list));
        list.remove(list.find("betty"));
        assertEquals(List.of(), all(list));
    }

    @Test
    void example3() {
        var list = newList();
        list.add("fred");
        list.add("wilma");
        list.add("betty");
        list.add("barney");

        assertEquals(List.of("fred", "wilma", "betty", "barney"), all(list));
        list.remove(list.find("wilma"));
        assertEquals(List.of("fred", "betty", "barney"), all(list));
        list.remove(list.find("barney"));
        assertEquals(List.of("fred", "betty"), all(list));

        list.add("wilma");
        list.add("barney");
        list.add("dino");
        assertEquals(List.of("fred", "betty", "wilma", "barney", "dino"), all(list));

        list.remove(list.find("betty"));
        assertEquals(List.of("fred", "wilma", "barney", "dino"), all(list));
    }

    @Test
    void work() {
        var list = newList();
        var rnd = new Random(9999);

        work(list, rnd, 8);
        work(list, rnd, 32);
        work(list, rnd, 128);

        System.out.println(all(list));
    }

    private void work(MyList<String> list, Random rnd, int size) {
        for (int i = 0; i < 1000; i++) {
            var s = ""+ rnd.nextInt(size);;
            var e = list.find(s);
            if (e == null) {
                list.add(s);
            } else {
                list.remove(e);
            }
        }
    }
}
