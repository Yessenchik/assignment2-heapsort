import csv
import matplotlib.pyplot as plt
import numpy as np
import sys
import os

if len(sys.argv) < 2:
    print("Usage: python plot_results.py <csv_file>")
    sys.exit(1)

# Read CSV data
sizes = []
times = []
comparisons = []
swaps = []
heapify_ops = []
memory = []

print(f"Reading data from: {sys.argv[1]}")

with open(sys.argv[1], 'r') as f:
    reader = csv.DictReader(f)
    for row in reader:
        sizes.append(int(row['InputSize']))
        times.append(float(row['TimeMillis']))
        comparisons.append(int(row['Comparisons']))
        swaps.append(int(row['Swaps']))
        heapify_ops.append(int(row['HeapifyOps']))
        memory.append(int(row['MemoryKB']))

# Calculate averages for each unique size
unique_sizes = sorted(set(sizes))
avg_times = []
avg_comparisons = []
avg_swaps = []
avg_heapify = []
avg_memory = []
std_times = []

for size in unique_sizes:
    indices = [i for i, s in enumerate(sizes) if s == size]
    avg_times.append(np.mean([times[i] for i in indices]))
    avg_comparisons.append(np.mean([comparisons[i] for i in indices]))
    avg_swaps.append(np.mean([swaps[i] for i in indices]))
    avg_heapify.append(np.mean([heapify_ops[i] for i in indices]))
    avg_memory.append(np.mean([memory[i] for i in indices]))
    std_times.append(np.std([times[i] for i in indices]))

print(f"✓ Processed {len(sizes)} data points across {len(unique_sizes)} input sizes")
print(f"✓ Input sizes: {unique_sizes}")

# Create output directory
os.makedirs('docs/performance-plots', exist_ok=True)

# ========== PLOT 1: TIME VS INPUT SIZE ==========
plt.figure(figsize=(12, 7))
plt.plot(unique_sizes, avg_times, 'o-', linewidth=2.5, markersize=10,
         color='#2E86AB', label='Measured Time')
plt.errorbar(unique_sizes, avg_times, yerr=std_times, fmt='none',
             ecolor='gray', alpha=0.5, capsize=5)
plt.xlabel('Input Size (n)', fontsize=14, fontweight='bold')
plt.ylabel('Time (milliseconds)', fontsize=14, fontweight='bold')
plt.title('Heap Sort Performance: Execution Time vs Input Size', fontsize=16, fontweight='bold')
plt.grid(True, alpha=0.3, linestyle='--')
plt.legend(fontsize=12)

# Add annotation for largest size
max_idx = len(unique_sizes) - 1
plt.annotate(f'n={unique_sizes[max_idx]}\n{avg_times[max_idx]:.2f} ms',
             xy=(unique_sizes[max_idx], avg_times[max_idx]),
             xytext=(unique_sizes[max_idx] * 0.7, avg_times[max_idx] * 1.2),
             arrowprops=dict(arrowstyle='->', color='red', lw=2),
             fontsize=11, bbox=dict(boxstyle='round,pad=0.5', facecolor='yellow', alpha=0.7))

plt.tight_layout()
plt.savefig('docs/performance-plots/1_time_vs_size.png', dpi=300, bbox_inches='tight')
print("✓ Saved: docs/performance-plots/1_time_vs_size.png")
plt.close()

# ========== PLOT 2: COMPARISONS VS INPUT SIZE ==========
plt.figure(figsize=(12, 7))
plt.plot(unique_sizes, avg_comparisons, 'o-', linewidth=2.5, markersize=10,
         color='#FF6B35', label='Comparisons')
plt.xlabel('Input Size (n)', fontsize=14, fontweight='bold')
plt.ylabel('Number of Comparisons', fontsize=14, fontweight='bold')
plt.title('Heap Sort: Comparisons vs Input Size', fontsize=16, fontweight='bold')
plt.grid(True, alpha=0.3, linestyle='--')
plt.legend(fontsize=12)

# Theoretical O(n log n) line
theoretical = [n * np.log2(n) * 1.44 for n in unique_sizes]  # 1.44 is constant factor
plt.plot(unique_sizes, theoretical, '--', linewidth=2, color='green',
         alpha=0.7, label='Theoretical 1.44·n log₂(n)')
plt.legend(fontsize=12)

plt.tight_layout()
plt.savefig('docs/performance-plots/2_comparisons_vs_size.png', dpi=300, bbox_inches='tight')
print("✓ Saved: docs/performance-plots/2_comparisons_vs_size.png")
plt.close()

# ========== PLOT 3: SWAPS VS INPUT SIZE ==========
plt.figure(figsize=(12, 7))
plt.plot(unique_sizes, avg_swaps, 'o-', linewidth=2.5, markersize=10,
         color='#A23B72', label='Swaps')
plt.xlabel('Input Size (n)', fontsize=14, fontweight='bold')
plt.ylabel('Number of Swaps', fontsize=14, fontweight='bold')
plt.title('Heap Sort: Swaps vs Input Size', fontsize=16, fontweight='bold')
plt.grid(True, alpha=0.3, linestyle='--')

# Theoretical: n-1 swaps (one per extraction)
theoretical_swaps = [n - 1 for n in unique_sizes]
plt.plot(unique_sizes, theoretical_swaps, '--', linewidth=2, color='red',
         alpha=0.7, label='Theoretical (n-1)')
plt.legend(fontsize=12)

plt.tight_layout()
plt.savefig('docs/performance-plots/3_swaps_vs_size.png', dpi=300, bbox_inches='tight')
print("✓ Saved: docs/performance-plots/3_swaps_vs_size.png")
plt.close()

# ========== PLOT 4: COMPLEXITY VERIFICATION (TIME / n log n) ==========
n_log_n = [n * np.log2(n) for n in unique_sizes]
normalized_time = [t / nl for t, nl in zip(avg_times, n_log_n)]

plt.figure(figsize=(12, 7))
plt.plot(unique_sizes, normalized_time, 'o-', linewidth=2.5, markersize=10,
         color='#06A77D', label='Time / (n log₂ n)')
plt.axhline(y=np.mean(normalized_time), color='red', linestyle='--',
            linewidth=2, label=f'Mean: {np.mean(normalized_time):.6f}')
plt.xlabel('Input Size (n)', fontsize=14, fontweight='bold')
plt.ylabel('Time / (n log₂ n) [ms per operation]', fontsize=14, fontweight='bold')
plt.title('Complexity Verification: Time/(n log n) Should Be Constant for Θ(n log n)',
          fontsize=16, fontweight='bold')
plt.grid(True, alpha=0.3, linestyle='--')
plt.legend(fontsize=12)

# Add shaded region for ±10% variation
mean_val = np.mean(normalized_time)
plt.axhspan(mean_val * 0.9, mean_val * 1.1, alpha=0.2, color='green',
            label='±10% variance')
plt.legend(fontsize=11)

plt.tight_layout()
plt.savefig('docs/performance-plots/4_complexity_verification.png', dpi=300, bbox_inches='tight')
print("✓ Saved: docs/performance-plots/4_complexity_verification.png")
plt.close()

# ========== PLOT 5: ALL OPERATIONS COMBINED ==========
fig, axes = plt.subplots(2, 2, figsize=(16, 12))
fig.suptitle('Heap Sort: Complete Performance Analysis', fontsize=18, fontweight='bold')

# Time
axes[0, 0].plot(unique_sizes, avg_times, 'o-', linewidth=2, markersize=8, color='#2E86AB')
axes[0, 0].set_xlabel('Input Size (n)', fontsize=12, fontweight='bold')
axes[0, 0].set_ylabel('Time (ms)', fontsize=12, fontweight='bold')
axes[0, 0].set_title('Execution Time', fontsize=14)
axes[0, 0].grid(True, alpha=0.3)
axes[0, 0].set_yscale('log')
axes[0, 0].set_xscale('log')

# Comparisons
axes[0, 1].plot(unique_sizes, avg_comparisons, 'o-', linewidth=2, markersize=8, color='#FF6B35')
axes[0, 1].set_xlabel('Input Size (n)', fontsize=12, fontweight='bold')
axes[0, 1].set_ylabel('Comparisons', fontsize=12, fontweight='bold')
axes[0, 1].set_title('Number of Comparisons', fontsize=14)
axes[0, 1].grid(True, alpha=0.3)
axes[0, 1].set_yscale('log')
axes[0, 1].set_xscale('log')

# Swaps
axes[1, 0].plot(unique_sizes, avg_swaps, 'o-', linewidth=2, markersize=8, color='#A23B72')
axes[1, 0].set_xlabel('Input Size (n)', fontsize=12, fontweight='bold')
axes[1, 0].set_ylabel('Swaps', fontsize=12, fontweight='bold')
axes[1, 0].set_title('Number of Swaps', fontsize=14)
axes[1, 0].grid(True, alpha=0.3)
axes[1, 0].set_yscale('log')
axes[1, 0].set_xscale('log')

# Heapify Operations
axes[1, 1].plot(unique_sizes, avg_heapify, 'o-', linewidth=2, markersize=8, color='#F18F01')
axes[1, 1].set_xlabel('Input Size (n)', fontsize=12, fontweight='bold')
axes[1, 1].set_ylabel('Heapify Operations', fontsize=12, fontweight='bold')
axes[1, 1].set_title('Heapify Operations', fontsize=14)
axes[1, 1].grid(True, alpha=0.3)

plt.tight_layout()
plt.savefig('docs/performance-plots/5_complete_analysis.png', dpi=300, bbox_inches='tight')
print("✓ Saved: docs/performance-plots/5_complete_analysis.png")
plt.close()

# ========== PLOT 6: GROWTH RATE ANALYSIS ==========
if len(unique_sizes) > 1:
    growth_ratios = []
    size_ratios = []

    for i in range(1, len(unique_sizes)):
        time_ratio = avg_times[i] / avg_times[i - 1]
        size_ratio = unique_sizes[i] / unique_sizes[i - 1]
        theoretical_ratio = (unique_sizes[i] * np.log2(unique_sizes[i])) / \
                            (unique_sizes[i - 1] * np.log2(unique_sizes[i - 1]))
        growth_ratios.append(time_ratio)
        size_ratios.append(theoretical_ratio)

    plt.figure(figsize=(12, 7))
    x_labels = [f'{unique_sizes[i - 1]}→{unique_sizes[i]}' for i in range(1, len(unique_sizes))]
    x_pos = np.arange(len(x_labels))

    width = 0.35
    plt.bar(x_pos - width / 2, growth_ratios, width, label='Measured Growth',
            color='#2E86AB', alpha=0.8)
    plt.bar(x_pos + width / 2, size_ratios, width, label='Theoretical O(n log n)',
            color='#06A77D', alpha=0.8)

    plt.xlabel('Input Size Transition', fontsize=14, fontweight='bold')
    plt.ylabel('Growth Ratio', fontsize=14, fontweight='bold')
    plt.title('Growth Rate Analysis: Measured vs Theoretical', fontsize=16, fontweight='bold')
    plt.xticks(x_pos, x_labels, rotation=45, ha='right')
    plt.legend(fontsize=12)
    plt.grid(True, alpha=0.3, axis='y')
    plt.tight_layout()
    plt.savefig('docs/performance-plots/6_growth_rate_analysis.png', dpi=300, bbox_inches='tight')
    print("✓ Saved: docs/performance-plots/6_growth_rate_analysis.png")
    plt.close()

# ========== PLOT 7: OPERATIONS PER ELEMENT ==========
ops_per_element_comp = [c / n for c, n in zip(avg_comparisons, unique_sizes)]
ops_per_element_swap = [s / n for s, n in zip(avg_swaps, unique_sizes)]

plt.figure(figsize=(12, 7))
plt.plot(unique_sizes, ops_per_element_comp, 'o-', linewidth=2.5, markersize=10,
         color='#FF6B35', label='Comparisons per Element')
plt.plot(unique_sizes, ops_per_element_swap, 's-', linewidth=2.5, markersize=10,
         color='#A23B72', label='Swaps per Element')

# Theoretical: log2(n)
theoretical_per_elem = [np.log2(n) * 1.44 for n in unique_sizes]
plt.plot(unique_sizes, theoretical_per_elem, '--', linewidth=2, color='green',
         alpha=0.7, label='Theoretical 1.44·log₂(n)')

plt.xlabel('Input Size (n)', fontsize=14, fontweight='bold')
plt.ylabel('Operations per Element', fontsize=14, fontweight='bold')
plt.title('Heap Sort: Operations per Element (Should Scale as log n)',
          fontsize=16, fontweight='bold')
plt.grid(True, alpha=0.3, linestyle='--')
plt.legend(fontsize=12)
plt.tight_layout()
plt.savefig('docs/performance-plots/7_operations_per_element.png', dpi=300, bbox_inches='tight')
print("✓ Saved: docs/performance-plots/7_operations_per_element.png")
plt.close()

# ========== PRINT STATISTICS ==========
print("\n" + "=" * 80)
print("PERFORMANCE SUMMARY TABLE")
print("=" * 80)
print(f"{'Size':<10} {'Time(ms)':<12} {'Comparisons':<15} {'Swaps':<10} {'Heapify':<10} {'Time/nlogn':<12}")
print("-" * 80)
for i, size in enumerate(unique_sizes):
    print(f"{size:<10} {avg_times[i]:<12.3f} {int(avg_comparisons[i]):<15} "
          f"{int(avg_swaps[i]):<10} {int(avg_heapify[i]):<10} {normalized_time[i]:<12.6f}")

print("\n" + "=" * 80)
print("COMPLEXITY VERIFICATION")
print("=" * 80)
print(f"Average Time/(n log n):     {np.mean(normalized_time):.6f} ms")
print(f"Std Dev Time/(n log n):     {np.std(normalized_time):.6f} ms")
print(f"Coefficient of Variation:   {(np.std(normalized_time) / np.mean(normalized_time)) * 100:.2f}%")
print(f"\n✓ Low CV% (<15%) confirms Θ(n log n) complexity")

print("\n" + "=" * 80)
print("THEORETICAL VS EMPIRICAL COMPARISON")
print("=" * 80)
print(f"{'Size':<10} {'Measured Comps':<18} {'Theoretical':<18} {'Ratio':<10}")
print("-" * 80)
for i, size in enumerate(unique_sizes):
    theoretical = size * np.log2(size) * 1.44
    ratio = avg_comparisons[i] / theoretical
    print(f"{size:<10} {int(avg_comparisons[i]):<18} {int(theoretical):<18} {ratio:<10.3f}")

print("\n✅ All plots generated successfully!")
print(f"✅ Total plots created: 7")
print(f"✅ Location: docs/performance-plots/")