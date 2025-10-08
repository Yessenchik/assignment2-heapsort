package metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracks performance metrics for algorithm analysis including comparisons,
 * swaps, array accesses, and execution time.
 *
 * This class provides detailed metrics collection for empirical validation
 * of theoretical complexity analysis.
 */
public class PerformanceTracker {

    // Core operation counters
    private long comparisons;
    private long swaps;
    private long arrayReads;
    private long arrayWrites;
    private long heapifyOperations;

    // Memory tracking
    private long peakMemoryUsage;
    private long initialMemory;

    // Timing
    private long startTime;
    private long endTime;

    // Data collection for multiple runs
    private final List<RunMetrics> runHistory;

    /**
     * Container for metrics from a single algorithm run
     */
    public static class RunMetrics {
        public final int inputSize;
        public final long comparisons;
        public final long swaps;
        public final long arrayAccesses;
        public final long heapifyOps;
        public final long timeNanos;
        public final long memoryBytes;
        public final String inputType;

        public RunMetrics(int inputSize, long comparisons, long swaps,
                          long arrayAccesses, long heapifyOps, long timeNanos,
                          long memoryBytes, String inputType) {
            this.inputSize = inputSize;
            this.comparisons = comparisons;
            this.swaps = swaps;
            this.arrayAccesses = arrayAccesses;
            this.heapifyOps = heapifyOps;
            this.timeNanos = timeNanos;
            this.memoryBytes = memoryBytes;
            this.inputType = inputType;
        }

        @Override
        public String toString() {
            return String.format("n=%d, comparisons=%d, swaps=%d, time=%.3fms, memory=%dKB",
                    inputSize, comparisons, swaps, timeNanos / 1_000_000.0, memoryBytes / 1024);
        }
    }

    public PerformanceTracker() {
        this.runHistory = new ArrayList<>();
        reset();
    }

    /**
     * Reset all counters for a new measurement
     */
    public void reset() {
        comparisons = 0;
        swaps = 0;
        arrayReads = 0;
        arrayWrites = 0;
        heapifyOperations = 0;
        peakMemoryUsage = 0;
        startTime = 0;
        endTime = 0;
    }

    /**
     * Start timing measurement
     */
    public void startTiming() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Suggest garbage collection for cleaner memory measurement
        try {
            Thread.sleep(50); // Give GC time to run
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        initialMemory = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
    }

    /**
     * Stop timing measurement
     */
    public void stopTiming() {
        endTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long currentMemory = runtime.totalMemory() - runtime.freeMemory();
        peakMemoryUsage = Math.max(peakMemoryUsage, currentMemory - initialMemory);
    }

    /**
     * Record a comparison operation
     */
    public void recordComparison() {
        comparisons++;
    }

    /**
     * Record multiple comparisons at once
     */
    public void recordComparisons(int count) {
        comparisons += count;
    }

    /**
     * Record a swap operation (counts as 2 writes + 2 reads)
     */
    public void recordSwap() {
        swaps++;
        arrayReads += 2;
        arrayWrites += 2;
    }

    /**
     * Record an array read operation
     */
    public void recordArrayRead() {
        arrayReads++;
    }

    /**
     * Record an array write operation
     */
    public void recordArrayWrite() {
        arrayWrites++;
    }

    /**
     * Record a heapify operation
     */
    public void recordHeapify() {
        heapifyOperations++;
    }

    /**
     * Save current metrics to run history
     */
    public void saveRun(int inputSize, String inputType) {
        long totalArrayAccesses = arrayReads + arrayWrites;
        long executionTime = endTime - startTime;

        RunMetrics metrics = new RunMetrics(
                inputSize, comparisons, swaps, totalArrayAccesses,
                heapifyOperations, executionTime, peakMemoryUsage, inputType
        );

        runHistory.add(metrics);
    }

    /**
     * Export all run metrics to CSV file
     */
    public void exportToCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // CSV Header
            writer.println("InputSize,InputType,Comparisons,Swaps,ArrayAccesses," +
                    "HeapifyOps,TimeNanos,TimeMillis,MemoryBytes,MemoryKB");

            // Data rows
            for (RunMetrics run : runHistory) {
                writer.printf("%d,%s,%d,%d,%d,%d,%d,%.3f,%d,%d%n",
                        run.inputSize,
                        run.inputType,
                        run.comparisons,
                        run.swaps,
                        run.arrayAccesses,
                        run.heapifyOps,
                        run.timeNanos,
                        run.timeNanos / 1_000_000.0,
                        run.memoryBytes,
                        run.memoryBytes / 1024
                );
            }
        }
    }

    /**
     * Print current metrics to console
     */
    public void printMetrics() {
        System.out.println("Performance Metrics");
        System.out.println("Comparisons:      " + comparisons);
        System.out.println("Swaps:            " + swaps);
        System.out.println("Array Reads:      " + arrayReads);
        System.out.println("Array Writes:     " + arrayWrites);
        System.out.println("Heapify Ops:      " + heapifyOperations);
        System.out.printf("Execution Time:   %.3f ms%n", getExecutionTimeMillis());
        System.out.printf("Memory Used:      %d KB%n", peakMemoryUsage / 1024);
        System.out.println("=");
    }

    /**
     * Print summary of all runs
     */
    public void printRunHistory() {
        System.out.println("\n=== Run History ===");
        for (int i = 0; i < runHistory.size(); i++) {
            System.out.printf("Run %d: %s%n", i + 1, runHistory.get(i));
        }
        System.out.println("=\n");
    }

    // Getters
    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayReads() { return arrayReads; }
    public long getArrayWrites() { return arrayWrites; }
    public long getHeapifyOperations() { return heapifyOperations; }
    public long getTotalArrayAccesses() { return arrayReads + arrayWrites; }
    public long getExecutionTimeNanos() { return endTime - startTime; }
    public double getExecutionTimeMillis() { return (endTime - startTime) / 1_000_000.0; }
    public long getPeakMemoryUsage() { return peakMemoryUsage; }
    public List<RunMetrics> getRunHistory() { return new ArrayList<>(runHistory); }
}