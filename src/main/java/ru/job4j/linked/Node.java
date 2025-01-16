package ru.job4j.linked;

/**
 * Класс Node превращён из mutable в immutable. Это достигнуто благодаря:
 * 1) Поля класса сделаны final.
 * 2) Убраны сеттеры и вместо них добавлен конструктор, принимающий сразу оба значения для своих полей.
 *      То есть, значения полей указываются один раз и больше не меняются.
 * @param <T>
 */
public class Node<T> {
    private final Node<T> next;
    private final T value;

    public Node(Node<T> next, T value) {
        this.next = next;
        this.value = value;
    }

    public Node<T> getNext() {
        return next;
    }

    public T getValue() {
        return value;
    }
}