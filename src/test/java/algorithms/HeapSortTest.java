package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for Heap Sort implementation.
 * Tests edge cases, correctness, and performance characteristics.
 */
class HeapSortTest {

    private HeapSort sorter;
    private HeapSort trackedSorter;
    private PerformanceTracker tracker;

    @BeforeEach
    void setUp() {
        sorter = new HeapSort();
        tracker = new PerformanceTracker();
        trackedSorter = new HeapSort(tracker);
    }

    // ===== EDGE CASE TESTS =====

    @Test
    @DisplayName("Test null array throws exception")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> sorter.sort(null));
    }

    @Test
    @DisplayName("Test empty array")
    void testEmptyArray() {
        int[] arr = {};
        sorter.sort(arr);
        assertEquals(0, arr.length);
        assertTrue(HeapSort.isSorted(arr));
    }

    @Test
    @DisplayName("Test single element array")
    void testSingleElement() {
        int[] arr = {42};
        sorter.sort(arr);
        assertArrayEquals(new int[]{42}, arr);
        assertTrue(HeapSort.isSorted(arr));
    }

    @Test
    @DisplayName("Test two elements - ascending")
    void testTwoElementsAscending() {
        int[] arr = {1, 2};
        sorter.sort(arr);
        assertArrayEquals(new int[]{1, 2}, arr);
    }

    @Test
    @DisplayName("Test two elements - descending")
    void testTwoElementsDescending() {
        int[] arr = {2, 1};
        sorter.sort(arr);
        assertArrayEquals(new int[]{1, 2}, arr);
    }

    // ===== DUPLICATE HANDLING TESTS =====

    @Test
    @DisplayName("Test all duplicate elements")
    void testAllDuplicates() {
        int[] arr = {5, 5, 5, 5, 5};
        sorter.sort(arr);
        assertArrayEquals(new int[]{5, 5, 5, 5, 5}, arr);
        assertTrue(HeapSort.isSorted(arr));
    }

    @Test
    @DisplayName("Test array with some duplicates")
    void testSomeDuplicates() {
        int[] arr = {4, 2, 7, 2, 9, 4, 1};
        int[] expected = {1, 2, 2, 4, 4, 7, 9};
        sorter.sort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Test array with many duplicates")
    void testManyDuplicates() {
        int[] arr = {3, 3, 3, 1, 1, 2, 2, 2, 2};
        int[] expected = {1, 1, 2, 2, 2, 2, 3, 3, 3};
        sorter.sort(arr);
        assertArrayEquals(expected, arr);
    }

    // ===== SORTED ARRAY TESTS =====

    @Test
    @DisplayName("Test already sorted array")
    void testAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] expected = arr.clone();
        sorter.sort(arr);
        assertArrayEquals(expected, arr);
        assertTrue(HeapSort.isSorted(arr));
    }

    @Test
    @DisplayName("Test reverse sorted array")
    void testReverseSorted() {
        int[] arr = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        sorter.sort(arr);
        assertArrayEquals(expected, arr);
    }

    // ===== RANDOM ARRAY TESTS =====

    @Test
    @DisplayName("Test small random array")
    void testSmallRandomArray() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        sorter.sort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Test medium random array")
    void testMediumRandomArray() {
        int[] arr = {38, 27, 43, 3, 9, 82, 10, 5, 15, 20, 33, 45, 67, 89, 12};
        sorter.sort(arr);
        assertTrue(HeapSort.isSorted(arr));
        assertEquals(15, arr.length);
    }

    // ===== NEGATIVE NUMBER TESTS =====

    @Test
    @DisplayName("Test array with negative numbers")
    void testNegativeNumbers() {
        int[] arr = {-5, 3, -2, 8, -10, 0, 15};
        int[] expected = {-10, -5, -2, 0, 3, 8, 15};
        sorter.sort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Test all negative numbers")
    void testAllNegative() {
        int[] arr = {-1, -5, -3, -2, -4};
        int[] expected = {-5, -4, -3, -2, -1};
        sorter.sort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Test mix of positive, negative, and zero")
    void testMixedSigns() {
        int[] arr = {0, -5, 10, -3, 7, 0, -1, 5};
        int[] expected = {-5, -3, -1, 0, 0, 5, 7, 10};
        sorter.sort(arr);
        assertArrayEquals(expected, arr);
    }

    // ===== LARGE ARRAY TESTS =====

    @ParameterizedTest
    @ValueSource(ints = {100, 500, 1000})
    @DisplayName("Test large random arrays of various sizes")
    void testLargeRandomArrays(int size) {
        Random rand = new Random(42); // Fixed seed for reproducibility
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(10000) - 5000; // Range: -5000 to 4999
        }

        sorter.sort(arr);
        assertTrue(HeapSort.isSorted(arr));
        assertEquals(size, arr.length);
    }

    // ===== PROPERTY-BASED TESTS =====

    @Test
    @DisplayName("Property: Sorted array should be non-decreasing")
    void testPropertyNonDecreasing() {
        Random rand = new Random(123);
        for (int trial = 0; trial < 50; trial++) {
            int size = rand.nextInt(100) + 10;
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = rand.nextInt(1000);
            }

            sorter.sort(arr);

            for (int i = 0; i < arr.length - 1; i++) {
                assertTrue(arr[i] <= arr[i + 1],
                        "Array not sorted at indices " + i + " and " + (i + 1));
            }
        }
    }

    @Test
    @DisplayName("Property: Sorted array should contain same elements")
    void testPropertySameElements() {
        int[] original = {5, 2, 8, 1, 9, 3, 7, 4, 6};
        int[] sorted = HeapSort.copyArray(original);

        Arrays.sort(original); // Use Java's sort for comparison
        sorter.sort(sorted);

        assertArrayEquals(original, sorted);
    }

    @Test
    @DisplayName("Property: Sorting should be idempotent")
    void testPropertyIdempotent() {
        int[] arr = {9, 3, 7, 1, 5, 2, 8, 4, 6};
        sorter.sort(arr);
        int[] firstSort = HeapSort.copyArray(arr);

        sorter.sort(arr);
        int[] secondSort = HeapSort.copyArray(arr);

        assertArrayEquals(firstSort, secondSort);
    }

    // ===== PERFORMANCE TRACKING TESTS =====

    @Test
    @DisplayName("Test performance tracking records operations")
    void testPerformanceTracking() {
        int[] arr = {5, 2, 8, 1, 9};

        tracker.reset();
        tracker.startTiming();
        trackedSorter.sort(arr);
        tracker.stopTiming();

        assertTrue(tracker.getComparisons() > 0, "Should record comparisons");
        assertTrue(tracker.getSwaps() > 0, "Should record swaps");
        assertTrue(tracker.getHeapifyOperations() > 0, "Should record heapify operations");
        assertTrue(tracker.getExecutionTimeNanos() > 0, "Should record execution time");
        assertTrue(HeapSort.isSorted(arr), "Array should be sorted");
    }

    @Test
    @DisplayName("Test complexity scaling for different input sizes")
    void testComplexityScaling() {
        int[] sizes = {10, 50, 100};
        long[] comparisons = new long[sizes.length];

        Random rand = new Random(456);
        for (int i = 0; i < sizes.length; i++) {
            int[] arr = new int[sizes[i]];
            for (int j = 0; j < sizes[i]; j++) {
                arr[j] = rand.nextInt(1000);
            }

            tracker.reset();
            trackedSorter.sort(arr);
            comparisons[i] = tracker.getComparisons();

            assertTrue(HeapSort.isSorted(arr));
        }

        // Verify that comparisons grow roughly as n log n
        // comparisons[i+1] / comparisons[i] should be > (sizes[i+1] / sizes[i])
        for (int i = 0; i < sizes.length - 1; i++) {
            double ratio = (double) comparisons[i + 1] / comparisons[i];
            double sizeRatio = (double) sizes[i + 1] / sizes[i];
            assertTrue(ratio > sizeRatio * 0.5,
                    "Comparisons should scale with O(n log n)");
        }
    }

    // ===== UTILITY METHOD TESTS =====

    @Test
    @DisplayName("Test isSorted utility method")
    void testIsSortedUtility() {
        assertTrue(HeapSort.isSorted(new int[]{1, 2, 3, 4, 5}));
        assertTrue(HeapSort.isSorted(new int[]{1}));
        assertTrue(HeapSort.isSorted(new int[]{}));
        assertTrue(HeapSort.isSorted(null));
        assertFalse(HeapSort.isSorted(new int[]{5, 4, 3, 2, 1}));
        assertFalse(HeapSort.isSorted(new int[]{1, 3, 2, 4}));
    }

    @Test
    @DisplayName("Test copyArray utility method")
    void testCopyArrayUtility() {
        int[] original = {1, 2, 3, 4, 5};
        int[] copy = HeapSort.copyArray(original);

        assertArrayEquals(original, copy);
        assertNotSame(original, copy); // Different objects

        copy[0] = 999;
        assertEquals(1, original[0]); // Original unchanged
    }

    // ===== CROSS-VALIDATION TEST =====

    @Test
    @DisplayName("Cross-validation with Java's Arrays.sort()")
    void testCrossValidation() {
        Random rand = new Random(789);

        for (int trial = 0; trial < 20; trial++) {
            int size = rand.nextInt(200) + 50;
            int[] arr1 = new int[size];
            for (int i = 0; i < size; i++) {
                arr1[i] = rand.nextInt(1000) - 500;
            }

            int[] arr2 = HeapSort.copyArray(arr1);

            Arrays.sort(arr1); // Java's sort
            sorter.sort(arr2);  // Our heap sort

            assertArrayEquals(arr1, arr2,
                    "HeapSort should produce same result as Arrays.sort()");
        }
    }
}