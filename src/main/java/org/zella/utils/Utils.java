package org.zella.utils;


import org.zella.config.IConfig;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zella.
 */
public class Utils {


    /**
     * шанс события
     *
     * @param zeroToOne [0.0f .. 1.0f]
     * @return
     */
    public static boolean shouldHappens(float zeroToOne) {
        Random r = new Random();
        return r.nextFloat() < zeroToOne;
    }

    private static final long MEGABYTE = 1024L * 1024L;

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = ThreadLocalRandom.current();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static int randInRange(IConfig.Range range) {
        return randInt(range.min, range.max);
    }

    public static int[] shuffleArray(int[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }

        return ar;
    }

    public static long[] randomLongs(final long count, final long min, final long max) {

        final Random rand = ThreadLocalRandom.current();

        //TODO test for max and minimum values
        final long[] randomNum = rand.longs(count, min, max + 1).toArray();
        return randomNum;
    }

    //TODO not tested
    public static long[] randomLongsNonDuplicates(final long count, final long min, final long max) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
// Note: use LinkedHashSet to maintain insertion order
        //TODO not efficient
        Set<Long> generated = new LinkedHashSet<>();
        while (generated.size() < count) {
            Long next = randLong(rng, min, max);
            generated.add(next);
        }
        return generated.stream().mapToLong(i -> i).toArray();
    }

    private static long randLong(Random random, long min, long max) {
        //TODO swith impl
        return min + ((long) (random.nextDouble() * (max + 1 - min)));
    }

    private static String generateString(Random rng, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    public static String generateString(int length) {
        return generateString(ThreadLocalRandom.current(), "abcdefghi123456zxcv", length);
    }


    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }


    public static <T> T randomElement(Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for (T t : coll) if (--num < 0) return t;
        throw new AssertionError();
    }
}
