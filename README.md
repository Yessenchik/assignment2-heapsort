# Heap Sort Implementation - Assignment 2

**Student B, Pair 2: Advanced Sorting Algorithms**  
**Algorithm**: Heap Sort with In-Place Implementation and Bottom-Up Heapify Optimization

---

## 📋 Algorithm Overview

**Heap Sort** is a comparison-based sorting algorithm that leverages the heap data structure. It operates in two main phases:

1. **Build Max-Heap**: Transform the input array into a max-heap using bottom-up heapification
2. **Extract Maximum**: Repeatedly extract the maximum element and restore the heap property

### Key Features
- ✅ **In-place sorting**: O(1) auxiliary space
- ✅ **Bottom-up heapify**: Optimized O(n) heap construction
- ✅ **Predictable performance**: Θ(n log n) for all cases
- ✅ **No recursion overhead**: Iterative heapify implementation

---

## 🧮 Complexity Analysis

### Time Complexity

| Case | Complexity | Explanation |
|------|------------|-------------|
| **Best Case** | Θ(n log n) | Even sorted arrays require full heap operations |
| **Average Case** | Θ(n log n) | Expected performance on random input |
| **Worst Case** | Θ(n log n) | Reverse-sorted or any configuration |

**Why Θ(n log n) for all cases?**
- Build heap: O(n) using bottom-up approach
- Extract max: n iterations × O(log n) heapify = O(n log n)
- Total: O(n) + O(n log n) = Θ(n log n)

### Space Complexity
- **Auxiliary Space**: Θ(1) - sorts in-place
- **Total Space**: Θ(n) - input array only

---

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Git

### Build and Run
```bash
# Clone repository
git clone <repository-url>
cd assignment2-heapsort

# Build project
mvn clean compile

# Run tests
mvn test

# Run benchmarks
mvn exec:java -Dexec.mainClass="cli.BenchmarkRunner"

# Create JAR
mvn package
java -jar target/assignment2-heapsort-1.0.0.jar
```
# HeapSort Algorithm with Performance Tracking

A Java implementation of the **Heap Sort** algorithm with integrated performance tracking for educational and benchmarking purposes.

---

## 📘 Features

- 🧩 **Heap Sort Implementation** — Efficient O(n log n) sorting algorithm
- ⏱️ **Performance Metrics** — Track comparisons, swaps, execution time, and memory usage
- 🧪 **Reusable API** — Easily integrate into other Java projects
- 🧹 **Clean Design** — Clear separation between algorithm and performance tracking

---

## 🚀 Programmatic Usage

```java
import algorithms.HeapSort;
import metrics.PerformanceTracker;

public class Example {
    public static void main(String[] args) {
        // Basic usage
        HeapSort sorter = new HeapSort();
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        sorter.sort(arr);
        // arr is now sorted: [11, 12, 22, 25, 34, 64, 90]

        // With performance tracking
        PerformanceTracker tracker = new PerformanceTracker();
        HeapSort trackedSorter = new HeapSort(tracker);

        tracker.startTiming();
        trackedSorter.sort(arr);
        tracker.stopTiming();

        tracker.printMetrics();
        // Outputs: comparisons, swaps, execution time, memory usage
    }
}
```
# Command Line I Benchmarking

```# Quick benchmark with default sizes
java -cp target/assignment2-heapsort-1.0.0.jar cli.BenchmarkRunner
```

# Create JAR
```
mvn package
java -jar target/assignment2-heapsort-1.0.0.jar
```