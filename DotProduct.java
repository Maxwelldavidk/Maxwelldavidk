import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class DotProduct {
    static ForkJoinPool POOL = new ForkJoinPool();
    static int CUTOFF;

    // Behavior should match Sequential.dotProduct
    // Your implementation must have linear work and log(n) span
    public static int dotProduct(int[] a, int[]b, int cutoff){
        DotProduct.CUTOFF = cutoff;
        return POOL.invoke(new DotProductTask(a, b, 0, a.length)); // TODO: add parameters to match your constructor
    }

    private static class DotProductTask extends RecursiveTask<Integer> {
        // TODO: select fields
        int[] a;
        int[] b;
        int lo;
        int hi;


        public DotProductTask(int[]a, int[] b, int lo, int hi) {
            // TODO: implement constructor
            this.a = a;
            this.b = b;
            this.lo = lo;
            this.hi = hi;
        }

        protected Integer compute() {
            //TODO: Implement.
            if(hi - lo <= DotProduct.CUTOFF){
                int sum = 0;
                for(int i = lo; i < hi; i++){
                    sum += a[i] * b[i];
                }
                return sum;
            } else{
                int mid = lo + (hi - lo) / 2;
                DotProductTask left = new DotProductTask(a, b, lo, mid);
                DotProductTask right = new DotProductTask(a, b, mid, hi);
                left.fork();
                int rightResult = right.compute();
                int leftResult = left.join();
                return leftResult + rightResult;
            }
        }
    }
}
