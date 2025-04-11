package ru.job4j.concurrent.pool;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RolColSumTest {

    @Test
    public void whenNeedToCompareResultsOfTwoMethods() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RolColSum.Sums[] expected = {
                new RolColSum.Sums(6, 12),
                new RolColSum.Sums(15, 15),
                new RolColSum.Sums(24, 18)
        };
        RolColSum.Sums[] linearResult = RolColSum.sum(matrix);
        RolColSum.Sums[] asyncResult = RolColSum.asyncSum(matrix);
        assertThat(linearResult).isEqualTo(asyncResult).isEqualTo(expected);
    }

    @Test
    public void whenUseSmallMatrixLinearSumWorksFaster() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        long startTimeLinear = System.currentTimeMillis();
        RolColSum.sum(matrix);
        long linearTimeResult = System.currentTimeMillis() - startTimeLinear;
        long startTimeAsync = System.currentTimeMillis();
        RolColSum.asyncSum(matrix);
        long asyncTimeResult = System.currentTimeMillis() - startTimeAsync;
        assertThat(linearTimeResult).isLessThanOrEqualTo(asyncTimeResult);
    }

    @Test
    public void whenUseBigMatrixAsyncSumWorksFaster() {
        int[][] matrix = RolColSum.matrixGenerator(10_000);
        long startTimeLinear = System.currentTimeMillis();
        RolColSum.sum(matrix);
        long linearTimeResult = System.currentTimeMillis() - startTimeLinear;
        long startTimeAsync = System.currentTimeMillis();
        RolColSum.asyncSum(matrix);
        long asyncTimeResult = System.currentTimeMillis() - startTimeAsync;
        assertThat(asyncTimeResult).isLessThan(linearTimeResult);
    }
}