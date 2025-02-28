package ru.job4j.concurrent.wait;

public class CountBarrier {
    private final Object monitor = this;

    private final int total;

    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    public void count() {
        synchronized (monitor) {
            count++;
            monitor.notifyAll();
        }
    }

    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Метод await() был успешно выполнен нитью: " + Thread.currentThread().getName());
            System.out.println("Ограничитель равен: " + total + ", счётчик равен: " + count);
        }
    }
}
