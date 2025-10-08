package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class HeapSortTest {

    @Test
    void empty() {
        int[] a = {};
        HeapSort.sort(a, new PerformanceTracker());
        assertArrayEquals(new int[]{}, a);
    }

    @Test
    void single() {
        int[] a = {42};
        HeapSort.sort(a, new PerformanceTracker());
        assertArrayEquals(new int[]{42}, a);
    }

    @Test
    void reverse() {
        int[] a = {5,4,3,2,1};
        HeapSort.sort(a, new PerformanceTracker());
        assertArrayEquals(new int[]{1,2,3,4,5}, a);
    }

    @Test
    void duplicates() {
        int[] a = {5,1,3,5,2,1,5};
        int[] expected = a.clone();
        Arrays.sort(expected);
        HeapSort.sort(a, new PerformanceTracker());
        assertArrayEquals(expected, a);
    }

    @Test
    void randomBig() {
        Random r = new Random(42);
        int[] a = r.ints(5000, -1000, 1000).toArray();
        int[] expected = a.clone();
        Arrays.sort(expected);
        HeapSort.sort(a, new PerformanceTracker());
        assertArrayEquals(expected, a);
    }
}