package ru.job4j.concurrent.wait.senderreceiver;

public class Data {
    private String packet;

    /* True if receiver should wait */
    /* False if sender should wait */
    private boolean transfer = true;

    public synchronized String receive() {
        while (transfer) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread Interrupted");
            }
        }
        transfer = true;

        String returnPacket = packet;
        notifyAll();
        return returnPacket;
    }

    public synchronized void send(String packet) {
        while (!transfer) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread Interrupted");
            }
        }
        transfer = false;

        this.packet = packet;
        notifyAll();
    }
}

/* Давайте разберемся, что здесь происходит:

* Переменная пакета обозначает данные, которые передаются по сети.
* У нас есть логическая переменная transfer, которую Отправитель и Получатель будут использовать для синхронизации:
    ** Если эта переменная имеет значение true, то получатель должен дождаться,
        пока отправитель отправит сообщение.
    ** Если оно ложное, отправитель должен дождаться получения сообщения получателем.
* Отправитель использует метод send() для отправки данных Получателю:
    ** Если передача ложна, мы будем ждать, вызвав wait() в этой ветке.
    ** Но когда это правда, мы переключаем статус, устанавливаем наше сообщение и вызываем notifyAll(),
        чтобы разбудить другие потоки, чтобы указать, что произошло важное событие,
        и они могут проверить, могут ли они продолжить выполнение.
* Аналогично, Получатель будет использовать метод receive():
    ** Если перевод был установлен отправителем в false, только тогда он продолжится,
        в противном случае мы вызовем wait() в этом потоке.
    ** Когда условие выполняется, мы переключаем статус,
        уведомляем все ожидающие потоки о необходимости пробуждения и возвращаем полученный пакет данных. */