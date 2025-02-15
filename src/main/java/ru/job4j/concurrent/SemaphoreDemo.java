package ru.job4j.concurrent;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Runnable task = () -> {
            try {
                System.out.println("Thread before acquire");
                semaphore.acquire();
                System.out.println("Нить выполнила задачу");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        System.out.println("Before start");
        new Thread(task).start();
        System.out.println("After start");
        Thread.sleep(3000);
        System.out.println("After sleep");
        semaphore.release(1);
        System.out.println("After release");
    }
}
