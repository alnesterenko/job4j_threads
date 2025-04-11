package ru.job4j.concurrent.pool;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

public class RolColSum {
    public static class Sums {
        private int rowSum;
        private int colSum;

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        public int getRowSum() {
            return rowSum;
        }

        public void setRowSum(int rowSum) {
            this.rowSum = rowSum;
        }

        public int getColSum() {
            return colSum;
        }

        public void setColSum(int colSum) {
            this.colSum = colSum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Sums sums)) {
                return false;
            }
            return getRowSum() == sums.getRowSum() && getColSum() == sums.getColSum();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getRowSum(), getColSum());
        }


        @Override
        public String toString() {
            return "Sums{"
                    + "сумма строки = " + rowSum
                    + ", сумма столбца = " + colSum
                    + '}';
        }
    }

    public static int[][] matrixGenerator(int size) {
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = i + j;
            }
        }
        return result;
    }

    /* Последовательные методы */

    public static Sums[] sum(int[][] matrix) {
        int matrixLength = matrix.length;
        Sums[] result = new Sums[matrixLength];
        for (int i = 0; i < matrixLength; i++) {
            result[i] = new Sums(sumInRow(matrix, i), sumInColumn(matrix, i));
        }
        return result;
    }

    private static int sumInColumn(int[][] matrix, int column) {
        int result = 0;
        for (int i = 0; i < matrix.length; i++) {
            result += matrix[i][column];
        }
        return result;
    }

    private static int sumInRow(int[][] matrix, int row) {
        int result = 0;
        for (int i = 0; i < matrix.length; i++) {
            result += matrix[row][i];
        }
        return result;
    }

    /* Асинхронные методы */

    public static Sums[] asyncSum(int[][] matrix) {
        int matrixLength = matrix.length;
        Sums[] result = IntStream.range(0, matrixLength)
                .parallel()
                .mapToObj((i) -> sumInRowAsync(matrix, i)
                        .thenCombine(sumInColumnAsync(matrix, i), Sums::new))
                .map(item -> {
                            try {
                                return item.get();
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                )
                .toArray(Sums[]::new);
        return result;
    }

    private static CompletableFuture<Integer> sumInColumnAsync(int[][] matrix, int column) {
        return CompletableFuture.supplyAsync(() -> sumInColumn(matrix, column));
    }

    private static CompletableFuture<Integer> sumInRowAsync(int[][] matrix, int row) {
        return CompletableFuture.supplyAsync(() -> sumInRow(matrix, row));
    }
}
