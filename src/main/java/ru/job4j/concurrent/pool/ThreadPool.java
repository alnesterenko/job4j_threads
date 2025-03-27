package ru.job4j.concurrent.pool;

import ru.job4j.concurrent.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>();

    public ThreadPool() {
        int size = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < size; i++) {
            Thread tempThread = new Thread(
                    () -> {
                        while (!tasks.isEmpty() || !Thread.currentThread().isInterrupted()) {
                            try {
                                tasks.poll().run();
                            } catch (InterruptedException e) {
                                Thread.interrupted();
                            }
                        }
                    }
            );
            tempThread.start();
            threads.add(tempThread);
        }
    }

    public void work(Runnable job) throws InterruptedException {
        tasks.offer(job);
    }

    public void shutdown() {
        for (Thread oneThread : threads) {
            oneThread.interrupt();
        }
    }

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool();
        for (int i = 0; i < 10; i++) {
            final int tempFinalI = i;
            try {
                threadPool.work(
                        () -> System.out.println("Задача номер: " + tempFinalI
                                + " выполнена потоком: " + Thread.currentThread().getName())
                );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        threadPool.shutdown();
    }
}
