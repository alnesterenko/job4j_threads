package ru.job4j.concurrent.pool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {

    private final ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public void emailTo(User user) {
        String username = user.username();
        String email = user.email();
        String subject = String.format("Notification %s to email %s.", username, email);
        String body = String.format("Add a new event to %s", username);
        pool.submit(() -> send(subject, body, email));
    }

    public void send(String subject, String body, String email) {
    }

    public void close() {
        pool.shutdown();
    }

    public static void main(String[] args) {
        EmailNotification emailNotification = new EmailNotification();
        List<User> listOfUsers = List.of(
                new User("Алексей", "alex@mail.ru"),
                new User("Андрей", "andrey@mail.ru"),
                new User("Василий", "vasiliy@gmail.com"),
                new User("Ростислав", "rostislav123@yandex.ru"),
                new User("Вячеслав", "foox@gmail.com"),
                new User("Сергей", "ser_gay@mail.ru")
        );
        listOfUsers.forEach(emailNotification::emailTo);
        emailNotification.close();
    }
}
