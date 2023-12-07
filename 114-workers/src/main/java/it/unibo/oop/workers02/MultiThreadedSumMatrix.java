package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix{
    int n;


    public MultiThreadedSumMatrix(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
    }

    private static class Worker extends Thread {
        private final double[][] matrix;
        private final int startpos;
        private final int nelem;
        private long res;

        Worker(final double[][] matrix, final int startpos, final int nelem){
            super();
            this.matrix = matrix;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        @SuppressWarnings("PMD.SystemPrintln")
        public void run() {
            System.out.println("Working from position " + startpos + "to position " + (startpos + nelem -1));
            for (int i = startpos; i < matrix.length && i < startpos + nelem; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    this.res += this.matrix[i][j];
                }
            }
        }

        public double getResult() {
            return this.res;
        }
    }

    @Override
    public double sum(double[][] matrix) {
        final int size = matrix.length % n + matrix.length / n;
        double sum = 0;
        final List<Worker> workers = new ArrayList<>(n);

        for (int i = 0; i < matrix.length; i += size) {
            workers.add(new Worker(matrix, i, size));
        }

        for(final Worker w: workers){
            w.start();
        }

        for(final Worker w : workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            
        }

        return sum;
    }
}
