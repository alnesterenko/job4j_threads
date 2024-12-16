package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> System.out.println("Имя первой нити: " + Thread.currentThread().getName())
        );
        Thread second = new Thread(
                () -> System.out.println("Имя второй нити: " + Thread.currentThread().getName())
        );
        System.out.println("Имя главной нити: " + Thread.currentThread().getName());
        System.out.println("Состояние первой нити ДО запуска метода start(): " + first.getState());
        System.out.println("Состояние второй нити ДО запуска метода start(): " + second.getState());
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED || second.getState() != Thread.State.TERMINATED) {
            System.out.println("Состояние первой нити ПОСЛЕ запуска метода start(): " + first.getState());
            System.out.println("Состояние второй нити ПОСЛЕ запуска метода start(): " + second.getState());

        }
        System.out.println("Работа завершена!");
    }
}