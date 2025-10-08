package cli;

import algorithms.HeapSort;
import metrics.PerformanceTracker;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class BenchmarkRunner {

    private static final int[] DEFAULT_SIZES = {100, 500, 1000, 5000, 10000, 50000, 100000};
    private static final int WARMUP_RUNS = 3;
    private static final int MEASUREMENT_RUNS = 5;

    public static void main(String[] args) {
        System.out.println("=== HEAP SORT PERFORMANCE BENCHMARK SUITE ===");
        System.out.println("Bottom-Up Heapify | In-Place Sorting\n");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            System.out.println();

            switch (choice) {
                case 1 -> runQuickBenchmark();
                case 2 -> runComprehensiveBenchmark();
                case 3 -> runCustomBenchmark(scanner);
                case 4 -> runInputDistributionAnalysis();
                case 5 -> runComplexityVerification();
                case 6 -> {
                    System.out.println("Exiting benchmark suite. Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    private static void printMenu() {
        System.out.println("Benchmark Options:");
        System.out.println("1. Quick Benchmark (default sizes)");
        System.out.println("2. Comprehensive Benchmark (with CSV export)");
        System.out.println("3. Custom Benchmark (specify size)");
        System.out.println("4. Input Distribution Analysis");
        System.out.println("5. Complexity Verification (n log n)");
        System.out.println("6. Exit");
    }

    private static void runQuickBenchmark() {
        System.out.println("=== QUICK BENCHMARK ===\n");

        PerformanceTracker tracker = new PerformanceTracker();

        for (int size : DEFAULT_SIZES) {
            int[] arr = generateRandomArray(size);
            HeapSort sorter = new HeapSort(tracker);

            tracker.reset();
            tracker.startTiming();
            sorter.sort(arr);
            tracker.stopTiming();

            System.out.printf("n = %-7d | Time: %8.3f ms | Comparisons: %10d | Swaps: %10d%n",
                    size, tracker.getExecutionTimeMillis(),
                    tracker.getComparisons(), tracker.getSwaps());
        }

        System.out.println();
    }

    private static void runComprehensiveBenchmark() {
        System.out.println("=== COMPREHENSIVE BENCHMARK ===");
        System.out.println("Running " + MEASUREMENT_RUNS + " trials per size with " +
                WARMUP_RUNS + " warmup runs...\n");

        PerformanceTracker tracker = new PerformanceTracker();

        for (int size : DEFAULT_SIZES) {
            System.out.println("Testing size n = " + size + "...");

            for (int w = 0; w < WARMUP_RUNS; w++) {
                int[] arr = generateRandomArray(size);
                HeapSort sorter = new HeapSort();
                sorter.sort(arr);
            }

            for (int run = 0; run < MEASUREMENT_RUNS; run++) {
                int[] arr = generateRandomArray(size);
                HeapSort sorter = new HeapSort(tracker);

                tracker.reset();
                tracker.startTiming();
                sorter.sort(arr);
                tracker.stopTiming();
                tracker.saveRun(size, "random");

                System.out.printf("  Run %d: %.3f ms%n", run + 1, tracker.getExecutionTimeMillis());
            }
            System.out.println();
        }

        try {
            String filename = "heap_sort_benchmark_" + System.currentTimeMillis() + ".csv";
            tracker.exportToCSV(filename);
            System.out.println("✓ Benchmark results exported to: " + filename + "\n");
        } catch (IOException e) {
            System.err.println("✗ Error exporting CSV: " + e.getMessage() + "\n");
        }

        tracker.printRunHistory();
    }

    private static void runCustomBenchmark(Scanner scanner) {
        System.out.print("Enter array size: ");
        int size = scanner.nextInt();

        System.out.print("Enter number of trials: ");
        int trials = scanner.nextInt();

        System.out.println("\n=== CUSTOM BENCHMARK (n = " + size + ") ===\n");

        PerformanceTracker tracker = new PerformanceTracker();

        for (int trial = 0; trial < trials; trial++) {
            int[] arr = generateRandomArray(size);
            HeapSort sorter = new HeapSort(tracker);

            tracker.reset();
            tracker.startTiming();
            sorter.sort(arr);
            tracker.stopTiming();

            System.out.printf("Trial %d:%n", trial + 1);
            tracker.printMetrics();
            System.out.println();
        }
    }

    private static void runInputDistributionAnalysis() {
        System.out.println("=== INPUT DISTRIBUTION ANALYSIS ===\n");

        int size = 10000;
        String[] distributions = {"Random", "Sorted", "Reverse", "Nearly Sorted", "Many Duplicates"};

        PerformanceTracker tracker = new PerformanceTracker();

        System.out.printf("Testing with n = %d across different distributions:%n%n", size);

        for (String dist : distributions) {
            int[] arr = generateArrayByDistribution(size, dist);
            HeapSort sorter = new HeapSort(tracker);

            tracker.reset();
            tracker.startTiming();
            sorter.sort(arr);
            tracker.stopTiming();
            tracker.saveRun(size, dist);

            System.out.printf("%-18s | Time: %8.3f ms | Comparisons: %10d%n",
                    dist, tracker.getExecutionTimeMillis(), tracker.getComparisons());
        }

        System.out.println("\n✓ All distributions tested successfully.\n");
    }

    private static void runComplexityVerification() {
        System.out.println("=== COMPLEXITY VERIFICATION ===");
        System.out.println("Verifying Θ(n log n) time complexity...\n");

        int[] sizes = {1000, 2000, 4000, 8000, 16000, 32000};
        double[] times = new double[sizes.length];

        PerformanceTracker tracker = new PerformanceTracker();

        System.out.println("Size (n)    | Time (ms)  | Time/n log n | Ratio to Previous");
        System.out.println("------------|------------|--------------|------------------");

        for (int i = 0; i < sizes.length; i++) {
            int size = sizes[i];
            int[] arr = generateRandomArray(size);
            HeapSort sorter = new HeapSort(tracker);

            tracker.reset();
            tracker.startTiming();
            sorter.sort(arr);
            tracker.stopTiming();

            times[i] = tracker.getExecutionTimeMillis();
            double normalizedTime = times[i] / (size * Math.log(size) / Math.log(2));

            if (i > 0) {
                double ratio = times[i] / times[i - 1];
                double expectedRatio = (double) sizes[i] * Math.log(sizes[i]) /
                        (sizes[i - 1] * Math.log(sizes[i - 1]));

                System.out.printf("%-11d | %10.3f | %12.6f | %.3f (expected: %.3f)%n",
                        size, times[i], normalizedTime, ratio, expectedRatio);
            } else {
                System.out.printf("%-11d | %10.3f | %12.6f | --%n",
                        size, times[i], normalizedTime);
            }
        }

        System.out.println("\n✓ Complexity verification complete.");
        System.out.println("  Time/n log n values should remain relatively constant for Θ(n log n) complexity.\n");
    }

    private static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(size * 10);
        }
        return arr;
    }

    private static int[] generateArrayByDistribution(int size, String distribution) {
        Random rand = new Random();
        int[] arr = new int[size];

        switch (distribution) {
            case "Random" -> {
                for (int i = 0; i < size; i++) {
                    arr[i] = rand.nextInt(size * 10);
                }
            }
            case "Sorted" -> {
                for (int i = 0; i < size; i++) {
                    arr[i] = i;
                }
            }
            case "Reverse" -> {
                for (int i = 0; i < size; i++) {
                    arr[i] = size - i;
                }
            }
            case "Nearly Sorted" -> {
                for (int i = 0; i < size; i++) {
                    arr[i] = i;
                }
                for (int i = 0; i < size / 20; i++) {
                    int idx1 = rand.nextInt(size);
                    int idx2 = rand.nextInt(size);
                    int temp = arr[idx1];
                    arr[idx1] = arr[idx2];
                    arr[idx2] = temp;
                }
            }
            case "Many Duplicates" -> {
                for (int i = 0; i < size; i++) {
                    arr[i] = rand.nextInt(10);
                }
            }
        }

        return arr;
    }
}