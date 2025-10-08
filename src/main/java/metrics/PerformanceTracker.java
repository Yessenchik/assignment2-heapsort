package metrics;

public final class PerformanceTracker {
    private long compares, swaps, reads, writes;
    private long startNs, elapsedNs;

    public void start() { startNs = System.nanoTime(); }
    public void stop()  { elapsedNs = System.nanoTime() - startNs; }

    public void cmp()   { compares++; }
    public void swap()  { swaps++; }
    public void read()  { reads++; }
    public void write() { writes++; }

    public long compares()  { return compares; }
    public long swaps()     { return swaps; }
    public long reads()     { return reads; }
    public long writes()    { return writes; }
    public long elapsedNs() { return elapsedNs; }

    @Override public String toString() {
        return String.format("time=%.3f ms, cmp=%d, swaps=%d, reads=%d, writes=%d",
                elapsedNs / 1_000_000.0, compares, swaps, reads, writes);
    }
}