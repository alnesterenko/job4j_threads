package ru.job4j.concurrent.cache;

public class OptimisticException extends Exception {

    public OptimisticException(String message) {
        super(message);
    }
}
