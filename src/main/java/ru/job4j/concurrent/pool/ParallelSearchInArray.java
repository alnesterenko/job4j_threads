package ru.job4j.concurrent.pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSearchInArray<V> extends RecursiveTask<Integer> {
    private final V[] array;
    private final int from;
    private final int to;
    private final V key;
    public static final int LINEAR_SEARCH_LIMIT = 10;

    public ParallelSearchInArray(V[] array, int from, int to, V key) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.key = key;
    }

    @Override
    protected Integer compute() {
        if (to - from < LINEAR_SEARCH_LIMIT) {
            return linearSearch();
        }
        int middle = (from + to) / 2;
        ParallelSearchInArray<V> leftSearch = new ParallelSearchInArray(array, from, middle, key);
        ParallelSearchInArray<V> rightSearch = new ParallelSearchInArray(array, middle + 1, to, key);
        leftSearch.fork();
        rightSearch.fork();
        return Math.max(leftSearch.join(), rightSearch.join());
    }

    private <V> Integer linearSearch() {
        int result = -1;
        for (int i = from; i <= to; i++) {
            if (key.equals(array[i])) {
                result = i;
                break;
            }
        }
        return result;
    }

    public static <V> Integer search(V[] array, V key) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ParallelSearchInArray<>(array, 0, array.length - 1, key));
    }
}
