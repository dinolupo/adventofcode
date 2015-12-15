package io.github.dinolupo.adventofcode;

import java.util.stream.IntStream;

/**
 * Created by dino on 14/12/15.
 */
public class Util {
    public static long compose(int hi, int lo) {
        return (((long) hi << 32) + unsigned(lo));
    }
    public static long unsigned(int x) {
        return x & 0xFFFFFFFFL;
    }

    public static int high(long x) {
        return (int) (x>>32);
    }
    public static int low(long x) {
        return (int) x;
    }

    static IntStream bytesToIntStream(byte[] bytes) {
        return IntStream.range(0, bytes.length)
                .map(i -> bytes[i] & 0xFF)
                ;
    }
}
