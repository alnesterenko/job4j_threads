package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {

    @Test
    void when3AddAnd3RemoveButMaximumSizeIs2() throws InterruptedException {
        SimpleBlockingQueue<Integer> blockingQueue = new SimpleBlockingQueue<>(2);
        List<Integer> result = new ArrayList<>();
        Thread producer = new Thread(
                () -> {
                    for (int i = 1; i < 4; i++) {
                        try {
                            blockingQueue.offer(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                },
                "Producer"
        );
        Thread consumer = new Thread(
                () -> {
                    for (int i = 1; i < 4; i++) {
                        try {
                            Integer tempResult = blockingQueue.poll();
                            result.add(tempResult);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                },
                "Consumer"
        );
        consumer.start();
        producer.start();
        consumer.join();
        producer.join();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).containsExactly(1, 2, 3);
    }
}