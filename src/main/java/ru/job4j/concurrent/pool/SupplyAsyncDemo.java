package ru.job4j.concurrent.pool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static ru.job4j.concurrent.pool.RunAsyncDemo.iWork;

public class SupplyAsyncDemo {

    public static CompletableFuture<String> buyProduct(String product) {
        return CompletableFuture.supplyAsync(
                () -> {
                    System.out.println("Сын: Мам/Пам, я пошел в магазин");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Сын: Мам/Пап, я купил " + product);
                    return product;
                }
        );
    }

    public static void supplyAsyncExample() throws Exception {
        CompletableFuture<String> bm = buyProduct("Молоко");
        iWork();
        System.out.println("Куплено: " + bm.get());
    }

    public static void thenAcceptExample() throws Exception {
        CompletableFuture<String> bm = buyProduct("Молоко");
        bm.thenAccept((product) -> System.out.println("Сын: Я убрал " + product + " в холодильник "));
        iWork();
        System.out.println("Куплено: " + bm.get());
    }

    public static void thenApplyExample() throws Exception {
        CompletableFuture<String> bm = buyProduct("Молоко")
                .thenApply((product) -> "Сын: я налил тебе в кружку " + product + ". Держи.");
        iWork();
        System.out.println(bm.get());
    }

    public static void main(String[] args) {
        try {
            supplyAsyncExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("----------------------------------------");
        try {
            thenAcceptExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("----------------------------------------");
        try {
            thenApplyExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
