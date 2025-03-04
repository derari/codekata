package code.exercism.romans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RomansTest {

    @Test
    void toRoman_i() {
        assertRoman("I", 1);
    }

    @Test
    void toRoman_ii() {
        assertRoman("II", 2);
    }

    @Test
    void toRoman_iv() {
        assertRoman("IV", 4);
    }

    @Test
    void toRoman_vii() {
        assertRoman("VII", 7);
    }

    @Test
    void toRoman_ix() {
        assertRoman("IX", 9);
    }

    @Test
    void toRoman_mcdxliii() {
        assertRoman("MCDXLIII", 1443);
    }

    @Test
    void toRoman_mmcmxcii() {
        assertRoman("MMCMXCII", 2992);
    }

    @Test
    void fromRoman_i() {
        assertRoman(1, "I");
    }

    @Test
    void fromRoman_ii() {
        assertRoman(2, "II");
    }

    @Test
    void fromRoman_iv() {
        assertRoman(4, "IV");
    }

    @Test
    void fromRoman_vii() {
        assertRoman(7, "VII");
    }

    @Test
    void fromRoman_ix() {
        assertRoman(9, "IX");
    }

    @Test
    void fromRoman_mcdxliii() {
        assertRoman(1443, "MCDXLIII");
    }

    @Test
    void fromRoman_mmcmxcii() {
        assertRoman(2992, "MMCMXCII");
    }

    @Test
    void fromRoman_iiiiiiiii() {
        assertRoman(9, "IIIIIIIII");
    }

    @Test
    void fromRoman_mmim() {
        assertRoman(2999, "MMIM");
    }

    @Test
    void fromRoman_immm() {
        assertRoman(2999, "IMMM");
    }

    @Test
    void fromRoman2_i() {
        assertRoman2(1, "I");
    }

    @Test
    void fromRoman2_ii() {
        assertRoman2(2, "II");
    }

    @Test
    void fromRoman2_iv() {
        assertRoman2(4, "IV");
    }

    @Test
    void fromRoman2_vii() {
        assertRoman2(7, "VII");
    }

    @Test
    void fromRoman2_ix() {
        assertRoman2(9, "IX");
    }

    @Test
    void fromRoman2_mcdxliii() {
        assertRoman2(1443, "MCDXLIII");
    }

    @Test
    void fromRoman2_mmcmxcii() {
        assertRoman2(2992, "MMCMXCII");
    }

    @Test
    void fromRoman2_iiiiiiiii() {
        assertRoman2(9, "IIIIIIIII");
    }

    @Test
    void fromRoman2_mmim() {
        assertRoman2(2999, "MMIM");
    }

    @Test
    void fromRoman2_immm() {
        assertRoman2(2999, "IMMM");
    }

    @Test
    void fromRoman2_iimmm() {
        assertRoman2(2998, "IIMMM");
    }

    @Test
    void fromRoman2_ixm() {
        assertRoman2(991, "IXM");
    }

    @Test
    void fromRoman2_xim() {
        assertRoman2(989, "XIM");
    }

    @Test
    void fromRoman2_mxm() {
        assertRoman2(1990, "MXM");
    }

    @Test
    void fromRoman2_ximxim() {
        assertRoman2(2 * 989, "XIMXIM");
    }
    
    private static void assertRoman(String expect, int number) {
        var r = Romans.toRoman(number);
        assertEquals(expect, r);
    }

    private static void assertRoman(int expect, String roman) {
        var r = Romans.fromRoman(roman);
        assertEquals(expect, r);
    }

    private static void assertRoman2(int expect, String roman) {
        var r = Romans.fromRoman2(roman);
        assertEquals(expect, r);
    }
}