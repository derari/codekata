package code.exercism.romans;

import java.util.stream.IntStream;

public class Romans {

    private static final char[] ONES = {'I', 'X', 'C', 'M'};
    private static final char[] FIVES = {'V', 'L', 'D'};

    private static final char[] CHARS = {'I', 'V', 'X', 'L', 'C', 'D', 'M'};
    private static final int[] VALUES = {1, 5, 10, 50, 100, 500, 1000};


    public static String toRoman(int n) {
        if (n < 0 || n > 3999) {
            throw new IllegalArgumentException("Number out of range: " + n);
        }
        var result = new StringBuilder();
        var x = 1000;
        for (var i = 3; i >= 0; i--) {
            append(n / x, i, result);
            n %= x;
            x /= 10;
        }
        return result.toString();
    }

    private static void append(int n, int i, StringBuilder result) {
        if (n == 0) return;
        if (n == 9) {
            result.append(ONES[i]);
            result.append(ONES[i + 1]);
            return;
        }
        if (n >= 5) {
            result.append(FIVES[i]);
            n -= 5;
        }
        if (n == 4) {
            result.append(ONES[i]);
            result.append(FIVES[i]);
            return;
        }
        for (var j = 0; j < n; j++) {
            result.append(ONES[i]);
        }
    }

    public static int fromRoman(String string) {
        var result = 0;
        var last = 0;
        for (var c : string.toUpperCase().toCharArray()) {
            var value = valueOf(c);
            if (value > last) {
                result += value - 2 * last;
            } else {
                result += value;
            }
            last = value;
        }
        return result;
    }

    public static int fromRoman2(String string) {
        var sums = new int[CHARS.length];
        for (var c : string.toUpperCase().toCharArray()) {
            int subtract = 0;
            for (int i = 0; i < CHARS.length; i++) {
                if (CHARS[i] == c) {
                    sums[i] += VALUES[i] - subtract;
                    break;
                }
                subtract += sums[i];
                sums[i] = 0;
            }
        }
        return IntStream.of(sums).sum();
    }

    private static int valueOf(char c) {
        for (var i = 0; i < ONES.length; i++) {
            if (ONES[i] == c) return (int) Math.pow(10, i);
        }
        for (var i = 0; i < FIVES.length; i++) {
            if (FIVES[i] == c) return 5 * (int) Math.pow(10, i);
        }
        throw new IllegalArgumentException("Invalid Roman numeral: " + c);
    }
}
