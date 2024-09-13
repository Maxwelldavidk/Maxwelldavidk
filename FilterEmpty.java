import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class FilterEmpty {
    static ForkJoinPool POOL = new ForkJoinPool();
    static int CUTOFF;

    public static String[] filterEmpty(String[] arr, int cutoff) {
        FilterEmpty.CUTOFF = cutoff;

        // Map non-empty strings to 1, and empty strings to 0
        int[] mapResult = new int[arr.length];
        POOL.invoke(new MapTask(arr, mapResult, 0, arr.length));

        // Compute prefix sum on the map result using ParallelPrefix
        int[] prefixSum = ParallelPrefix.prefixSum(mapResult, cutoff);

        // Initialize result array
        int resultLength = prefixSum[arr.length - 1];
        String[] result = new String[resultLength];

        // Populate result array using prefix sum and map result
        POOL.invoke(new PopulateResultTask(arr, mapResult, prefixSum, result, 0, arr.length));

        return result;
    }

    // map non-empty strings to 1 and empty strings to 0
    private static class MapTask extends RecursiveAction {
        String[] arr;
        int[] mapResult;
        int lo, hi;

        public MapTask(String[] arr, int[] mapResult, int lo, int hi) {
            this.arr = arr;
            this.mapResult = mapResult;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected void compute() {
            if (hi - lo <= FilterEmpty.CUTOFF) {
                for (int i = lo; i < hi; i++) {
                    if (arr[i].isEmpty()) {
                        mapResult[i] = 0;
                    } else {
                        mapResult[i] = 1;
                    }
                }
            } else {
                int mid = (lo + hi) / 2;
                MapTask left = new MapTask(arr, mapResult, lo, mid);
                MapTask right = new MapTask(arr, mapResult, mid, hi);
                left.fork();
                right.compute();
                left.join();
            }
        }
    }

    // populate the result array using prefix sum and map result
    private static class PopulateResultTask extends RecursiveAction {
        String[] arr;
        int[] mapResult;
        int[] prefixSum;
        String[] result;
        int lo, hi;

        public PopulateResultTask(String[] arr, int[] mapResult, int[] prefixSum, String[] result, int lo, int hi) {
            this.arr = arr;
            this.mapResult = mapResult;
            this.prefixSum = prefixSum;
            this.result = result;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected void compute() {
            if (hi - lo <= FilterEmpty.CUTOFF) {
                for (int i = lo; i < hi; i++) {
                    if (mapResult[i] == 1) {
                        result[prefixSum[i] - 1] = arr[i];
                    }
                }
            } else {
                int mid = (lo + hi) / 2;
                PopulateResultTask left = new PopulateResultTask(arr, mapResult, prefixSum, result, lo, mid);
                PopulateResultTask right = new PopulateResultTask(arr, mapResult, prefixSum, result, mid, hi);
                left.fork();
                right.compute();
                left.join();
            }
        }
    }
}