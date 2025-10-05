package algorithms;

import metrics.PerformanceTracker;

/**
 * In-place Heap Sort implementation with bottom-up heapify optimization.
 *
 * ALGORITHM OVERVIEW:
 * Heap Sort is a comparison-based sorting algorithm that uses a binary heap data structure.
 * It works in two phases:
 * 1. Build Max-Heap: Transform the input array into a max-heap using bottom-up heapify
 * 2. Extract Maximum: Repeatedly extract the maximum element and maintain heap property
 *
 * COMPLEXITY ANALYSIS:
 * - Time Complexity: Θ(n log n) for all cases (best, average, worst)
 * - Space Complexity: Θ(1) - sorts in-place with only O(1) auxiliary space
 *
 * OPTIMIZATIONS IMPLEMENTED:
 * - Bottom-up heapify: More efficient heap construction in O(n) instead of O(n log n)
 * - In-place sorting: No additional arrays needed
 * - Iterative implementation: Avoids recursive call overhead for heapify
 *
 * @author Student B, Pair 2
 * @version 1.0
 */
public class HeapSort {

    private final PerformanceTracker tracker;
    private final boolean enableTracking;

    /**
     * Constructor with performance tracking enabled
     */
    public HeapSort(PerformanceTracker tracker) {
        this.tracker = tracker;
        this.enableTracking = (tracker != null);
    }

    /**
     * Constructor without performance tracking
     */
    public HeapSort() {
        this.tracker = null;
        this.enableTracking = false;
    }

    /**
     * Sort an integer array in ascending order using Heap Sort algorithm.
     *
     * @param arr the array to be sorted (modified in-place)
     * @throws IllegalArgumentException if array is null
     */
    public void sort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }

        if (arr.length <= 1) {
            return; // Already sorted
        }

        int n = arr.length;

        // Phase 1: Build Max-Heap using bottom-up approach
        // Start from the last non-leaf node and heapify all nodes bottom-up
        // Last non-leaf node is at index (n/2 - 1)
        buildMaxHeap(arr, n);

        // Phase 2: Extract elements from heap one by one
        // Move current root (maximum) to end, reduce heap size, and heapify
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end (largest element to its final position)
            swap(arr, 0, i);

            // Heapify the reduced heap (exclude the sorted portion)
            heapifyDown(arr, i, 0);
        }
    }

    /**
     * Build a max-heap from an unsorted array using bottom-up approach.
     *
     * OPTIMIZATION: Bottom-up heap construction is more efficient than
     * inserting elements one by one. It runs in O(n) time instead of O(n log n).
     *
     * MATHEMATICAL PROOF OF O(n) COMPLEXITY:
     * - Height of heap = log n
     * - Nodes at height h: n / 2^(h+1)
     * - Cost of heapifying node at height h: O(h)
     * - Total cost = Σ(h=0 to log n) [n / 2^(h+1)] * h = O(n)
     *
     * @param arr the array to heapify
     * @param n the size of the heap
     */
    private void buildMaxHeap(int[] arr, int n) {
        // Start from the last non-leaf node and move up
        // Last non-leaf node index = (n / 2) - 1
        for (int i = (n / 2) - 1; i >= 0; i--) {
            heapifyDown(arr, n, i);
        }
    }

    /**
     * Maintain max-heap property by moving element down the tree.
     * Uses iterative approach to avoid recursion overhead.
     *
     * INVARIANT: At each step, the subtrees rooted at left and right children
     * are valid max-heaps, but the element at index i might violate the heap property.
     *
     * @param arr the array representing the heap
     * @param heapSize the current size of the heap
     * @param i the index of the element to heapify
     */
    private void heapifyDown(int[] arr, int heapSize, int i) {
        if (enableTracking) {
            tracker.recordHeapify();
        }

        // Use iterative approach instead of recursion for better performance
        int current = i;

        while (true) {
            int largest = current;
            int left = 2 * current + 1;   // Left child index
            int right = 2 * current + 2;  // Right child index

            // Check if left child exists and is greater than current largest
            if (left < heapSize) {
                if (enableTracking) {
                    tracker.recordComparison();
                    tracker.recordArrayRead(); // Reading arr[left]
                    tracker.recordArrayRead(); // Reading arr[largest]
                }
                if (arr[left] > arr[largest]) {
                    largest = left;
                }
            }

            // Check if right child exists and is greater than current largest
            if (right < heapSize) {
                if (enableTracking) {
                    tracker.recordComparison();
                    tracker.recordArrayRead(); // Reading arr[right]
                    tracker.recordArrayRead(); // Reading arr[largest]
                }
                if (arr[right] > arr[largest]) {
                    largest = right;
                }
            }

            // If largest is not the current node, swap and continue
            if (largest != current) {
                swap(arr, current, largest);
                current = largest; // Move down to the swapped position
            } else {
                break; // Heap property is satisfied
            }
        }
    }

    /**
     * Swap two elements in the array.
     *
     * @param arr the array
     * @param i index of first element
     * @param j index of second element
     */
    private void swap(int[] arr, int i, int j) {
        if (enableTracking) {
            tracker.recordSwap();
        }

        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Verify if an array is sorted in ascending order.
     * Useful for testing and validation.
     *
     * @param arr the array to check
     * @return true if sorted, false otherwise
     */
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

    /**
     * Create a copy of an array.
     *
     * @param arr the array to copy
     * @return a new array with the same elements
     */
    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] copy = new int[arr.length];
        System.arraycopy(arr, 0, copy, 0, arr.length);
        return copy;
    }
}