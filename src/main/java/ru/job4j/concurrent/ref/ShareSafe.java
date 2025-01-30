package ru.job4j.concurrent.ref;

public class ShareSafe {
    public static void main(String[] args) throws InterruptedException {
        UserCache cache = new UserCache();
        User user = User.of("main");
        cache.add(user);
        Thread first = new Thread(() -> {
            User luser1 = User.of("first");
            cache.add(luser1);
            User luser2 = User.of("second");
            cache.add(luser2);
            User luser3 = User.of("third");
            cache.add(luser3);
            user.setName(Thread.currentThread().getName());
        });
        Thread second = new Thread(() -> {
            User luser1 = User.of("fourth");
            cache.add(luser1);
            User luser2 = User.of("fifth");
            cache.add(luser2);
            User luser3 = User.of("sixth");
            cache.add(luser3);
            cache.findById(1).setName(Thread.currentThread().getName());
        });
        first.start();
        second.start();
        first.join();
        second.join();
        cache.findAll().stream().map(e -> e.getName()).forEach(System.out::println);
    }
}
