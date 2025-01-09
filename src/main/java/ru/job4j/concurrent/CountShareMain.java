package ru.job4j.concurrent;

public class CountShareMain {
    public static void main(String[] args) throws InterruptedException {
        Count count = new Count();
        Thread first = new Thread(count::increment);
        Thread second = new Thread(count::increment);
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println(count.get());
    }
}

class Count {
    private int value;

    public void increment() {
        value++;
    }

    public int get() {
        return value;
    }
}

/* Все операции, где данные зависят от начального состояния не атомарны.
 Эти операции можно описать через процесс "проверить и выполнить". */
