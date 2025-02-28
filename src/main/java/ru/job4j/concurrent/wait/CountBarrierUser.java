package ru.job4j.concurrent.wait;

public class CountBarrierUser {
    public static void main(String[] args) {
        CountBarrier countBarrier = new CountBarrier(100);
        Thread firstThread = new Thread(
                () -> {
                    for (int i = 0; i < 60; i++) {
                        countBarrier.count();
                    }
                },
                "firstThread"
        );
        Thread secondThread = new Thread(
                () -> {
                    for (int i = 0; i < 60; i++) {
                        countBarrier.count();
                    }
                },
                "secondThread"
        );
        Thread thirdThread = new Thread(
                countBarrier::await,
                "thirdThread"
        );
        Thread fourthThread = new Thread(
                countBarrier::await,
                "fourthThread"
        );
        thirdThread.start();
        firstThread.start();
        secondThread.start();
        fourthThread.start();
    }
}
