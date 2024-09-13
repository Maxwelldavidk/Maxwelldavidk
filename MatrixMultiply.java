import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class MatrixMultiply {
    static ForkJoinPool POOL = new ForkJoinPool();
    static int CUTOFF;

    // Behavior should match Sequential.multiply.
    // Ignoring the initialization of arrays, your implementation should have n^3 work and log(n) span
    public static int[][] multiply(int[][] a, int[][] b, int cutoff){
        MatrixMultiply.CUTOFF = cutoff;
        int[][] product = new int[a.length][b[0].length];
        POOL.invoke(new MatrixMultiplyAction(a, b, product, 0, a.length,0, b[0].length)); // TODO: add parameters to match your constructor
        return product;
    }

    // Behavior should match the 2d version of Sequential.dotProduct.
    // Your implementation must have linear work and log(n) span
    public static int dotProduct(int[][] a, int[][] b, int row, int col, int cutoff){
        MatrixMultiply.CUTOFF = cutoff;
        return POOL.invoke(new DotProductTask(a, b, row, col, 0, a.length)); // TODO: add parameters to match your constructor
    }

    private static class MatrixMultiplyAction extends RecursiveAction{
        // TODO: select fields
        int[][] a;
        int[][] b;
        int[][] product;
        int rowLo;
        int rowHi;
        int colLo;
        int colHi;


        public MatrixMultiplyAction(int[][]a, int[][] b, int[][] product, int rowLo, int rowHi, int colLo, int colHi){
            // TODO: implement constructor
            this.a = a;
            this.b = b;
            this.product = product;
            this.rowLo = rowLo;
            this.rowHi = rowHi;
            this.colLo = colLo;
            this.colHi = colHi;
        }

        public void compute(){
            // TODO: implement
            int rows = rowHi - rowLo;
            int cols = colHi - colLo;
            if(rows <= MatrixMultiply.CUTOFF && cols <= MatrixMultiply.CUTOFF){
                for(int i = rowLo; i < rowHi; i++){
                    for(int j = colLo; j < colHi; j++){
                        product[i][j] = dotProduct(a, b, i, j, MatrixMultiply.CUTOFF);
                    }
                }
            } else {
                int midRow = (rowLo + rowHi) / 2;
                int midCol = (colLo + colHi) / 2;

                MatrixMultiplyAction topLeft = new MatrixMultiplyAction(a, b, product, rowLo, midRow, colLo, midCol);
                MatrixMultiplyAction topRight = new MatrixMultiplyAction(a, b, product, rowLo, midRow, midCol, colHi);
                MatrixMultiplyAction bottomLeft = new MatrixMultiplyAction(a, b, product, midRow, rowHi, colLo, midCol);
                MatrixMultiplyAction bottomRight = new MatrixMultiplyAction(a, b, product, midRow, rowHi, midCol, colHi);

                topLeft.fork();
                topRight.fork();
                bottomLeft.fork();
                bottomRight.compute();

                topLeft.join();
                topRight.join();
                bottomLeft.join();

            }

        }

    }

    private static class DotProductTask extends RecursiveTask<Integer>{
        // TODO: select fields
        int[][] a;
        int[][] b;
        int[][] product;
        int row;
        int col;
        int lo;
        int hi;

        public DotProductTask(int[][]a, int[][] b, int row, int col, int lo, int hi){
            // TODO: implement constructor
            this.a = a;
            this.b = b;
            this.row = row;
            this.col = col;
            this.lo = lo;
            this.hi = hi;
        }

        public Integer compute(){
            // TODO: implement
            if ( hi - lo <= MatrixMultiply.CUTOFF){
                int sum = 0;
                for(int i = lo; i < hi; i++){
                    sum += a[row][i] * b[i][col];
                }
                return sum;
            } else {
                int mid = (lo + hi) / 2;
                DotProductTask left = new DotProductTask(a, b, row, col, lo, mid);
                DotProductTask right = new DotProductTask(a, b, row, col, mid, hi);
                left.fork();
                int rightResult = right.compute();
                int leftResult = left.join();

                return leftResult + rightResult;
            }

        }

    }

}