package algorithms;

import metrics.PerformanceTracker;

/**
 * Max-Heap implementation with increase-key and extract-max operations.
 * Supports both heap operations and in-place heap sort.
 *
 * COMPLEXITY ANALYSIS:
 * - Build Heap: Θ(n) using bottom-up heapify
 * - Extract-Max: Θ(log n)
 * - Increase-Key: Θ(log n)
 * - Heap Sort: Θ(n log n) for all cases
 * - Space: Θ(1) auxiliary space
 *
 * @author Student B, Pair 2
 */
public class HeapSort {

    private final PerformanceTracker tracker;
    private final boolean enableTracking;

    public HeapSort(PerformanceTracker tracker) {
        this.tracker = tracker;
        this.enableTracking = (tracker != null);
    }

    public HeapSort() {
        this.tracker = null;
        this.enableTracking = false;
    }

    /**
     * Sort array in ascending order using heap sort algorithm.
     *
     * TIME COMPLEXITY:
     * - Best/Average/Worst: Θ(n log n)
     *
     * SPACE COMPLEXITY: Θ(1)
     *
     * @param arr array to sort (modified in-place)
     * @throws IllegalArgumentException if array is null
     */
    public void sort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }

        if (arr.length <= 1) {
            return;
        }

        int n = arr.length;

        // Phase 1: Build max-heap using bottom-up approach O(n)
        buildMaxHeap(arr, n);

        // Phase 2: Extract elements from heap O(n log n)
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);
            heapifyDown(arr, i, 0);
        }
    }

    /**
     * Build max-heap from unsorted array using bottom-up approach.
     *
     * TIME: O(n) - more efficient than top-down O(n log n)
     * PROOF: Σ(h=0 to log n) [n/2^(h+1)] * h = O(n)
     *
     * @param arr array to heapify
     * @param n heap size
     */
    private void buildMaxHeap(int[] arr, int n) {
        for (int i = (n / 2) - 1; i >= 0; i--) {
            heapifyDown(arr, n, i);
        }
    }

    /**
     * Maintain max-heap property by moving element down.
     * Uses iterative approach to avoid recursion overhead.
     *
     * TIME: O(log n)
     * SPACE: O(1)
     *
     * @param arr heap array
     * @param heapSize current heap size
     * @param i index to heapify from
     */
    private void heapifyDown(int[] arr, int heapSize, int i) {
        if (enableTracking) {
            tracker.recordHeapify();
        }

        int current = i;

        while (true) {
            int largest = current;
            int left = 2 * current + 1;
            int right = 2 * current + 2;

            if (left < heapSize) {
                if (enableTracking) {
                    tracker.recordComparison();
                    tracker.recordArrayRead();
                    tracker.recordArrayRead();
                }
                if (arr[left] > arr[largest]) {
                    largest = left;
                }
            }

            if (right < heapSize) {
                if (enableTracking) {
                    tracker.recordComparison();
                    tracker.recordArrayRead();
                    tracker.recordArrayRead();
                }
                if (arr[right] > arr[largest]) {
                    largest = right;
                }
            }

            if (largest != current) {
                swap(arr, current, largest);
                current = largest;
            } else {
                break;
            }
        }
    }

    /**
     * Move element up to maintain max-heap property.
     * Used for increase-key operation.
     *
     * TIME: O(log n)
     * SPACE: O(1)
     *
     * @param arr heap array
     * @param i index to heapify from
     */
    private void heapifyUp(int[] arr, int i) {
        if (enableTracking) {
            tracker.recordHeapify();
        }

        while (i > 0) {
            int parent = (i - 1) / 2;

            if (enableTracking) {
                tracker.recordComparison();
                tracker.recordArrayRead();
                tracker.recordArrayRead();
            }

            if (arr[i] > arr[parent]) {
                swap(arr, i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    /**
     * Extract maximum element from max-heap.
     *
     * TIME: O(log n)
     *
     * @param arr heap array
     * @param heapSize current heap size
     * @return maximum element
     * @throws IllegalArgumentException if heap is empty
     */
    public int extractMax(int[] arr, int heapSize) {
        if (heapSize <= 0) {
            throw new IllegalArgumentException("Heap is empty");
        }

        int max = arr[0];
        arr[0] = arr[heapSize - 1];
        heapifyDown(arr, heapSize - 1, 0);

        return max;
    }

    /**
     * Increase value of element at index i to newValue.
     *
     * TIME: O(log n)
     *
     * @param arr heap array
     * @param i index of element
     * @param newValue new value (must be >= current value)
     * @throws IllegalArgumentException if newValue < current value
     */
    public void increaseKey(int[] arr, int i, int newValue) {
        if (newValue < arr[i]) {
            throw new IllegalArgumentException("New value must be >= current value");
        }

        arr[i] = newValue;
        heapifyUp(arr, i);
    }

    /**
     * Insert new element into max-heap.
     *
     * TIME: O(log n)
     *
     * @param arr heap array (must have space for new element)
     * @param heapSize current heap size
     * @param value value to insert
     * @return new heap size
     */
    public int insert(int[] arr, int heapSize, int value) {
        if (heapSize >= arr.length) {
            throw new IllegalArgumentException("Heap is full");
        }

        arr[heapSize] = Integer.MIN_VALUE;
        increaseKey(arr, heapSize, value);

        return heapSize + 1;
    }

    /**
     * Get maximum element without removing it.
     *
     * TIME: O(1)
     *
     * @param arr heap array
     * @param heapSize current heap size
     * @return maximum element
     */
    public int getMax(int[] arr, int heapSize) {
        if (heapSize <= 0) {
            throw new IllegalArgumentException("Heap is empty");
        }
        return arr[0];
    }

    private void swap(int[] arr, int i, int j) {
        if (enableTracking) {
            tracker.recordSwap();
        }

        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static boolean isSorted(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return true;
        }

        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMaxHeap(int[] arr, int heapSize) {
        for (int i = 0; i < heapSize; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < heapSize && arr[i] < arr[left]) {
                return false;
            }
            if (right < heapSize && arr[i] < arr[right]) {
                return false;
            }
        }
        return true;
    }

    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] copy = new int[arr.length];
        System.arraycopy(arr, 0, copy, 0, arr.length);
        return copy;
    }
}