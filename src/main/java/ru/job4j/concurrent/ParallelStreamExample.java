package ru.job4j.concurrent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParallelStreamExample {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> multiplication = list.stream()
                .reduce((left, right) -> left * right);
        System.out.println(multiplication.get());

        Stream<Integer> stream = list.parallelStream();
        System.out.println(stream.isParallel());
        Optional<Integer> secondMultiplication = stream.reduce((left, right) -> left * right);
        System.out.println(secondMultiplication.get());

        IntStream parallel = IntStream.range(1, 100).parallel();
        System.out.println(parallel.isParallel());
        IntStream sequential = parallel.sequential();
        System.out.println(sequential.isParallel());

        list.stream().parallel().peek(System.out::println).toList();

        list.stream().parallel().forEach(System.out::println);

        list.stream().parallel().forEachOrdered(System.out::println);
        /* Для сохранения порядка следования элементов можно воспользоваться методов forEachOrdered().
        * Метод forEachOrdered() не сортирует, а именно сохраняет исходный порядок следования элементов */


    }
}
