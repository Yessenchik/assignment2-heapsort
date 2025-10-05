package metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracks performance metrics for algorithm analysis including comparisons,
 * swaps, array accesses, and execution time.
 */
public class PerformanceTracker {

    private long comparisons;
    private long swaps;
    private long arrayReads;
    private long arrayWrites;
    private long heapifyOperations;
    private long peakMemoryUsage;
    private long initialMemory;
    private long startTime;
    private long endTime;
    private final List<RunMetrics> runHistory;

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

    public void startTiming() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        initialMemory = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
    }

    public void stopTiming() {
        endTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long currentMemory = runtime.totalMemory() - runtime.freeMemory();
        peakMemoryUsage = Math.max(peakMemoryUsage, currentMemory - initialMemory);
    }

    public void recordComparison() {
        comparisons++;
    }

    public void recordComparisons(int count) {
        comparisons += count;
    }

    public void recordSwap() {
        swaps++;
        arrayReads += 2;
        arrayWrites += 2;
    }

    public void recordArrayRead() {
        arrayReads++;
    }

    public void recordArrayWrite() {
        arrayWrites++;
    }

    public void recordHeapify() {
        heapifyOperations++;
    }

    public void saveRun(int inputSize, String inputType) {
        long totalArrayAccesses = arrayReads + arrayWrites;
        long executionTime = endTime - startTime;

        RunMetrics metrics = new RunMetrics(
                inputSize, comparisons, swaps, totalArrayAccesses,
                heapifyOperations, executionTime, peakMemoryUsage, inputType
        );

        runHistory.add(metrics);
    }

    public void exportToCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("InputSize,InputType,Comparisons,Swaps,ArrayAccesses," +
                    "HeapifyOps,TimeNanos,TimeMillis,MemoryBytes,MemoryKB");

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

    public void printMetrics() {
        System.out.println("=== Performance Metrics ===");
        System.out.println("Comparisons:      " + comparisons);
        System.out.println("Swaps:            " + swaps);
        System.out.println("Array Reads:      " + arrayReads);
        System.out.println("Array Writes:     " + arrayWrites);
        System.out.println("Heapify Ops:      " + heapifyOperations);
        System.out.printf("Execution Time:   %.3f ms%n", getExecutionTimeMillis());
        System.out.printf("Memory Used:      %d KB%n", peakMemoryUsage / 1024);
        System.out.println("=========================");
    }

    public void printRunHistory() {
        System.out.println("\n=== Run History ===");
        for (int i = 0; i < runHistory.size(); i++) {
            System.out.printf("Run %d: %s%n", i + 1, runHistory.get(i));
        }
        System.out.println("==================\n");
    }

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