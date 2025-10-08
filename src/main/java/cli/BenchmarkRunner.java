package cli;

import algorithms.HeapSort;
import metrics.PerformanceTracker;

import java.io.PrintWriter;
import java.nio.file.*;
import java.util.Random;
import java.util.function.IntUnaryOperator;

public final class BenchmarkRunner {
    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final String[] PATTERNS = {"random", "sorted", "reverse", "nearly_sorted"};
    private static final int TRIALS = 5;
    private static final long BASE_SEED = 42L;
    private static final String OUTPUT = "bench/heapsort_bench.csv";

    public static void main(String[] args) throws Exception {
        Path out = Paths.get(args != null && args.length > 0 ? args[0] : OUTPUT);
        if (out.getParent() != null) Files.createDirectories(out.getParent());

        warmup();

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(out))) {
            pw.println("algo,n,pattern,trial,time_ms,compares,reads,writes,swaps");

            for (int n : SIZES) {
                for (String pattern : PATTERNS) {
                    int[] base = generate(n, pattern, BASE_SEED);
                    for (int t = 1; t <= TRIALS; t++) {
                        int[] a = base.clone();
                        PerformanceTracker m = new PerformanceTracker();
                        HeapSort.sort(a, m);
                        if (!isSorted(a))
                            throw new AssertionError("Not sorted: n=" + n + ", pattern=" + pattern);
                        double ms = m.elapsedNs() / 1_000_000.0;
                        pw.printf("HeapSort,%d,%s,%d,%.3f,%d,%d,%d,%d%n",
                                n, pattern, t, ms, m.compares(), m.reads(), m.writes(), m.swaps());
                    }
                    System.out.printf("Done: n=%-7d pattern=%-14s%n", n, pattern);
                }
            }
        }
        System.out.println("CSV written to: " + out.toAbsolutePath());
    }

    private static int[] generate(int n, String pattern, long seed) {
        switch (pattern) {
            case "sorted":  return sequence(n, i -> i);
            case "reverse": return sequence(n, i -> n - 1 - i);
            case "random":  return randomArray(n, seed);
            case "nearly_sorted": return nearlySorted(n, seed, 0.05);
            default: throw new IllegalArgumentException("Unknown pattern: " + pattern);
        }
    }

    private static int[] sequence(int n, IntUnaryOperator f) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = f.applyAsInt(i);
        return a;
    }

    private static int[] randomArray(int n, long seed) {
        Random rnd = new Random(seed);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
        return a;
    }

    private static int[] nearlySorted(int n, long seed, double p) {
        int[] a = sequence(n, i -> i);
        Random rnd = new Random(seed ^ 0x9E3779B97F4A7C15L);
        int swaps = Math.max(1, (int)Math.round(n * p));
        for (int k = 0; k < swaps; k++) {
            int i = rnd.nextInt(n), j = rnd.nextInt(n);
            int t = a[i]; a[i] = a[j]; a[j] = t;
        }
        return a;
    }

    private static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) if (a[i - 1] > a[i]) return false;
        return true;
    }

    private static void warmup() {
        int[] a = randomArray(50_000, 123);
        for (int i = 0; i < 2; i++) { int[] c = a.clone(); HeapSort.sort(c, new PerformanceTracker()); }
    }
}