package ru.job4j.threads.threadlocal;

public class ThreadLocalDemo {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        Thread first = new FirstThread();
        Thread second = new SecondThread();
        threadLocal.set("Это поток main.");
        System.out.println(threadLocal.get());
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
