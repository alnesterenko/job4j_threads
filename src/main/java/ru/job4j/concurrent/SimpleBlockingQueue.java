package ru.job4j.concurrent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("queue")
    private final Queue<T> queue = new LinkedList<>();

    private final int maxSize;

    public SimpleBlockingQueue(int size) {
        this.maxSize = size;
    }

    public void offer(T value) throws InterruptedException {
        synchronized (queue) {
            while (queue.size() >= maxSize) {
                queue.wait();
            }
            queue.offer(value);
            queue.notifyAll();
        }
    }

    public T poll() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();
            }
            T result = queue.poll();
            queue.notifyAll();
            return result;
        }
    }
}