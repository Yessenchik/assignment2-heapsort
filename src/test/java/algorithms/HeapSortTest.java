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
    @DisplayName("Test two elements ascending")
    void testTwoElementsAscending() {
        int[] arr = {1, 2};
        sorter.sort(arr);
        assertArrayEquals(new int[]{1, 2}, arr);
    }

    @Test
    @DisplayName("Test two elements descending")
    void testTwoElementsDescending() {
        int[] arr = {2, 1};
        sorter.sort(arr);
        assertArrayEquals(new int[]{1, 2}, arr);
    }

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

    @ParameterizedTest
    @ValueSource(ints = {100, 500, 1000})
    @DisplayName("Test large random arrays")
    void testLargeRandomArrays(int size) {
        Random rand = new Random(42);
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(10000) - 5000;
        }

        sorter.sort(arr);
        assertTrue(HeapSort.isSorted(arr));
        assertEquals(size, arr.length);
    }

    @Test
    @DisplayName("Property: sorted array is non-decreasing")
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
                assertTrue(arr[i] <= arr[i + 1]);
            }
        }
    }

    @Test
    @DisplayName("Property: same elements after sorting")
    void testPropertySameElements() {
        int[] original = {5, 2, 8, 1, 9, 3, 7, 4, 6};
        int[] sorted = HeapSort.copyArray(original);

        Arrays.sort(original);
        sorter.sort(sorted);

        assertArrayEquals(original, sorted);
    }

    @Test
    @DisplayName("Property: sorting is idempotent")
    void testPropertyIdempotent() {
        int[] arr = {9, 3, 7, 1, 5, 2, 8, 4, 6};
        sorter.sort(arr);
        int[] firstSort = HeapSort.copyArray(arr);

        sorter.sort(arr);
        int[] secondSort = HeapSort.copyArray(arr);

        assertArrayEquals(firstSort, secondSort);
    }

    @Test
    @DisplayName("Test performance tracking")
    void testPerformanceTracking() {
        int[] arr = {5, 2, 8, 1, 9};

        tracker.reset();
        tracker.startTiming();
        trackedSorter.sort(arr);
        tracker.stopTiming();

        assertTrue(tracker.getComparisons() > 0);
        assertTrue(tracker.getSwaps() > 0);
        assertTrue(tracker.getHeapifyOperations() > 0);
        assertTrue(tracker.getExecutionTimeNanos() > 0);
        assertTrue(HeapSort.isSorted(arr));
    }

    @Test
    @DisplayName("Test complexity scaling")
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

        for (int i = 0; i < sizes.length - 1; i++) {
            double ratio = (double) comparisons[i + 1] / comparisons[i];
            double sizeRatio = (double) sizes[i + 1] / sizes[i];
            assertTrue(ratio > sizeRatio * 0.5);
        }
    }

    @Test
    @DisplayName("Cross-validation with Arrays.sort")
    void testCrossValidation() {
        Random rand = new Random(789);

        for (int trial = 0; trial < 20; trial++) {
            int size = rand.nextInt(200) + 50;
            int[] arr1 = new int[size];
            for (int i = 0; i < size; i++) {
                arr1[i] = rand.nextInt(1000) - 500;
            }

            int[] arr2 = HeapSort.copyArray(arr1);

            Arrays.sort(arr1);
            sorter.sort(arr2);

            assertArrayEquals(arr1, arr2);
        }
    }

    @Test
    @DisplayName("Test extract-max operation")
    void testExtractMax() {
        int[] arr = {10, 5, 15, 3, 7, 12, 20};
        HeapSort heapSorter = new HeapSort();

        // Build max heap first
        int[] heap = HeapSort.copyArray(arr);
        heapSorter.sort(heap); // This builds heap then sorts, we need just heap

        // Actually build heap properly
        heap = new int[]{20, 15, 12, 10, 7, 5, 3}; // Manual max heap

        int max = heapSorter.extractMax(heap, 7);
        assertEquals(20, max);
        assertTrue(HeapSort.isMaxHeap(heap, 6));
    }

    @Test
    @DisplayName("Test increase-key operation")
    void testIncreaseKey() {
        int[] heap = {20, 15, 12, 10, 7, 5, 3};
        HeapSort heapSorter = new HeapSort();

        heapSorter.increaseKey(heap, 6, 25);

        assertTrue(HeapSort.isMaxHeap(heap, 7));
        assertEquals(25, heap[0]); // Should bubble to top
    }

    @Test
    @DisplayName("Test increase-key with invalid value")
    void testIncreaseKeyInvalid() {
        int[] heap = {20, 15, 12};
        HeapSort heapSorter = new HeapSort();

        assertThrows(IllegalArgumentException.class, () -> {
            heapSorter.increaseKey(heap, 1, 10);
        });
    }

    @Test
    @DisplayName("Test extract-max on empty heap")
    void testExtractMaxEmpty() {
        int[] heap = {};
        HeapSort heapSorter = new HeapSort();

        assertThrows(IllegalArgumentException.class, () -> {
            heapSorter.extractMax(heap, 0);
        });
    }

    @Test
    @DisplayName("Test insert operation")
    void testInsert() {
        int[] heap = new int[10];
        heap[0] = 20;
        heap[1] = 15;
        heap[2] = 12;
        int heapSize = 3;

        HeapSort heapSorter = new HeapSort();
        heapSize = heapSorter.insert(heap, heapSize, 18);

        assertEquals(4, heapSize);
        assertTrue(HeapSort.isMaxHeap(heap, heapSize));
    }

    @Test
    @DisplayName("Test getMax operation")
    void testGetMax() {
        int[] heap = {20, 15, 12, 10, 7};
        HeapSort heapSorter = new HeapSort();

        int max = heapSorter.getMax(heap, 5);
        assertEquals(20, max);
        assertEquals(20, heap[0]); // Should not modify heap
    }
}