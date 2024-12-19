package ru.job4j.threads.threadlocal;

import java.util.Arrays;
import java.util.List;

public class ThreadLocalDemo {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    public static ThreadLocal<List<String>> threadLocalStringList = new ThreadLocal<>();

    public static void initialList() {
        threadLocalStringList.set(Arrays.asList("one", "two", "three"));
    }

    public static void printList() {
        threadLocalStringList.get().stream()
                .forEach(System.out::println);
    }

    public static void changeList() {
        threadLocalStringList.set(threadLocalStringList.get().stream()
                .map(elem -> elem + " Из нити: " + Thread.currentThread().getName())
                .toList());
    }

    public static void main(String[] args) throws InterruptedException {
        Thread first = new FirstThread();
        Thread second = new SecondThread();
        ThreadLocalDemo.initialList();
        threadLocal.set("Это поток main.");
        System.out.println(threadLocal.get());
        ThreadLocalDemo.printList();
        ThreadLocalDemo.changeList();
        ThreadLocalDemo.printList();
        first.setPriority(10);
        System.out.println("Установленные приоритеты: "
                + Thread.currentThread().getName() + " = " + Thread.currentThread().getPriority()
                + ", " + first.getName() + " = " + first.getPriority()
                + ", " + second.getName() + " = " + second.getPriority());
        first.start();
        second.start();
        first.join();
        second.join();
    }

    /* Памятка!
    * Будьте внимательны, так как ThreadLocal изолирует только ссылки на объекты.
    *  Если в разных нитях записать в эту переменную один и тот же объект,
    *  то при работе с этим объектом проявятся все проблемы многопоточности. */
}
