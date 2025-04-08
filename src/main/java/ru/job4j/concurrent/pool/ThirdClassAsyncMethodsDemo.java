package ru.job4j.concurrent.pool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static ru.job4j.concurrent.pool.RunAsyncDemo.goToTrash;
import static ru.job4j.concurrent.pool.RunAsyncDemo.iWork;
import static ru.job4j.concurrent.pool.SupplyAsyncDemo.buyProduct;

public class ThirdClassAsyncMethodsDemo {

    public static void thenComposeExample() throws Exception {
        CompletableFuture<String> result = goToTrash().thenCompose(a -> buyProduct("Milk"));
        result.get(); /* wait calculations;*/
    }

    public static void thenCombineExample() throws Exception {
        CompletableFuture<String> result = buyProduct("Самогон")
                .thenCombine(buyProduct("Хлеб"), (r1, r2) -> "Куплены " + r1 + " и " + r2 + " и " + r1
                        + ". Чтобы два раза не ходить! )))");
        iWork();
        System.out.println(result.get());
    }

    public static CompletableFuture<Void> washHands(String name) {
        return CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + ", моет руки");
        });
    }

    public static void allOfExample() throws Exception {
        CompletableFuture<Void> all = CompletableFuture.allOf(
                washHands("Папа"), washHands("Мама"),
                washHands("Ваня"), washHands("Боря")
                /* Задачи выполняются в случайном порядке */
        );
        TimeUnit.SECONDS.sleep(3);
    }

    public static CompletableFuture<String> whoWashHands(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return name + ", моет руки";
        });
    }

    public static void anyOfExample() throws Exception {
        CompletableFuture<Object> first = CompletableFuture.anyOf(
                whoWashHands("Папа"), whoWashHands("Мама"),
                whoWashHands("Ваня"), whoWashHands("Боря")
                /* Выполняется только первая случайно выбранная задача */
        );
        System.out.println("Кто сейчас моет руки?");
        TimeUnit.SECONDS.sleep(1);
        System.out.println(first.get());
    }

    public static void main(String[] args) {
        try {
            thenComposeExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("----------------------------------");
        try {
            CompletableFuture<Void> result2 = buyProduct("Пиво").thenCompose(b -> goToTrash());
            result2.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("----------------------------------");
        try {
            thenCombineExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("----------------------------------");
        try {
            allOfExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("----------------------------------");
        try {
            anyOfExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
