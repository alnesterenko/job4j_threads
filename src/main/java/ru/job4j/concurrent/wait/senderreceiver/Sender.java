package ru.job4j.concurrent.wait.senderreceiver;

import java.util.concurrent.ThreadLocalRandom;

public class Sender implements Runnable {
    private Data data;

    public Sender(Data data) {
        this.data = data;
    }

    public void run() {
        String[] packets = {
                "First packet",
                "Second packet",
                "Third packet",
                "Fourth packet",
                "End"
        };

        for (String packet : packets) {
            data.send(packet);

            /* Thread.sleep() to mimic heavy server-side processing */
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread Interrupted");
            }
        }
    }
}

/* Давайте подробнее рассмотрим этот Sender:

* Мы создаем несколько случайных пакетов данных, которые будут отправляться по сети в массиве packets[].
* Для каждого пакета мы просто вызываем send().
* Затем мы вызываем Thread.sleep() со случайным интервалом,
    чтобы имитировать тяжелую обработку на стороне сервера. */
