package algorithms;

import metrics.PerformanceTracker;
import java.util.Arrays;

public final class HeapSort {
    private HeapSort() {}

    public static void sort(int[] a, PerformanceTracker m) {
        if (a == null || a.length < 2) return;
        if (m != null) m.start();
        final int n = a.length;
        for (int i = (n >>> 1) - 1; i >= 0; i--) siftDown(a, i, n, m);
        for (int end = n - 1; end > 0; end--) {
            swap(a, 0, end, m);
            siftDown(a, 0, end, m);
        }
        if (m != null) m.stop();
    }

    private static void siftDown(int[] a, int i, int heapSize, PerformanceTracker m) {
        int val = read(a, i, m);
        int half = heapSize >>> 1;
        while (i < half) {
            int left = (i << 1) + 1;
            int right = left + 1;
            int bestChild = left;
            int bcVal = read(a, left, m);
            if (right < heapSize) {
                int rVal = read(a, right, m);
                cmp(m);
                if (rVal > bcVal) { bestChild = right; bcVal = rVal; }
            }
            cmp(m);
            if (bcVal <= val) break;
            write(a, i, bcVal, m);
            i = bestChild;
        }
        write(a, i, val, m);
    }

    private static int read(int[] a, int i, PerformanceTracker m) { if (m != null) m.read(); return a[i]; }
    private static void write(int[] a, int i, int v, PerformanceTracker m) { if (m != null) m.write(); a[i] = v; }
    private static void swap(int[] a, int i, int j, PerformanceTracker m) {
        if (i == j) return;
        int t = read(a, i, m);
        write(a, i, read(a, j, m), m);
        write(a, j, t, m);
        if (m != null) m.swap();
    }
    private static void cmp(PerformanceTracker m) { if (m != null) m.cmp(); }

    public static void main(String[] args) {
        int[] arr = {5,2,9,1,7,3,8,4,6};
        PerformanceTracker m = new PerformanceTracker();
        sort(arr, m);
        System.out.println(Arrays.toString(arr));
        System.out.println(m);
    }
}