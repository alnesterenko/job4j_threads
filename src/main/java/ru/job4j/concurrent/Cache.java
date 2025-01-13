package ru.job4j.concurrent;

public final class Cache {
    private static Cache cache;

    public static synchronized Cache getInstance() {
        if (cache == null) {
            cache = new Cache();
        }
        return cache;
    }

    /* Всё что ниже, нужно только для того, чтобы проверить, что всё правильно работает */

    public void printString(String string) {
        System.out.println(string);
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new ThreadWork());
        Thread t2 = new Thread(new ThreadWork());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static class ThreadWork implements Runnable {
        @Override
        public void run() {
            System.out.println("Начало работы нити! " + Thread.currentThread().getName());
            Cache cache = Cache.getInstance();
            cache.printString("Объект cache сейчас используется нитью " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Завершение работы нити! " + Thread.currentThread().getName());
        }
    }
}