package code.codewars.alpinist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlpinistTest {

    @Test
    void test1() {
        var alpinist = new Alpinist("""
                014
                212
                441
                """);
        var path = alpinist.path();
        assertEquals(5, path.size());
        assertEquals(Point.points("00 10 11 21 22"), path);
    }

    @Test
    void test2() {
        var alpinist = new Alpinist("""
                01010
                01020
                01020
                00020
                """);
        var path = alpinist.path();
        assertEquals(Point.points("""
                00 01 02 03
                13 23 22 21
                20 30
                40 41 42 43
                """), path);
    }

    @Test
    void test3() {
        var alpinist = new Alpinist("""
                00100
                69990
                69091
                69990
                66665
                """);
        var path = alpinist.path();
        assertEquals(Point.points("00 01 02 03 04 14 24 34 44"), path);
    }
}
