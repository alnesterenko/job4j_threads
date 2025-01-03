package ru.job4j.threads;

import ru.job4j.service.FileDownloadParamsValidator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    private String getNewNameOfFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss");
        return LocalDateTime.now().format(formatter);
    }

    /**
     * Метод, вычисляющий необходимую задержку при скачивании.
     * 1) Делим лиммит(переданный вторым параметром в командной строке) на размер буффера,
     *      и получаем: коэффициент соотношения.
     * 2) Делим один миллион наносекунд(в одной долесекунде) на коэффициент соотношения,
     *      и получаем: время(в наносекундах), которое было бы потрачено на скачивание одного буффера,
     *      если бы скорость соответствовала лимиту(переданому вторым параметром в командной строке).
     * 3) Если РЕАЛЬНОЕ время, которое было потрачено на скачивание одного буффера, меньше,
     *      чем должно быть, то находим между ними разницу. ЭТО И БУДЕТ НЕОБХОДИМАЯ ЗАДЕРЖКА.
     *    А если наоборот, то задержка не нужна.
     * @param bufferSize размер буффера, используемый при скачивании. В байтах.
     * @param downloadPer РЕАЛЬНОЕ время скачивания.
     * @return необходимая задержка. В милисекундах.
     */
    private double calculateDownloadDelay(int bufferSize, long downloadPer) {
        int speedLimitBytesPerOneMillis = Integer.parseInt(FileDownloadParamsValidator.getResultMap().get("speed"));
        double coefficient = (double) speedLimitBytesPerOneMillis / bufferSize;
        var timeDownloadOneBuffer = (long) (1_000_000 / coefficient);
        return timeDownloadOneBuffer > downloadPer ? (double) (timeDownloadOneBuffer - downloadPer) / 1_000_000 : 0;
    }

    @Override
    public void run() {
        int bufferSize = 512;
        var parameters = FileDownloadParamsValidator.getResultMap();
        var file = new File(getNewNameOfFile() + "." + parameters.get("extension"));
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            var dataBuffer = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                var downloadAt = System.nanoTime();
                output.write(dataBuffer, 0, bytesRead);
                var downloadPer = System.nanoTime() - downloadAt;
                System.out.println("Read " + bufferSize + " bytes : " + downloadPer + " nano.");
                var speedBytesPerMillisecond = (double) bufferSize / downloadPer * 1_000_000;
                System.out.println("Скорость скачивания: " + speedBytesPerMillisecond + " байт за одну милисекунду");
                var delay = calculateDownloadDelay(bufferSize, downloadPer);
                System.out.println("Необходимая задержка: " + delay);
                long realDelay = Math.round(delay);
                System.out.println("Задержка, которая будет реально выставлена: " + realDelay);
                try {
                    Thread.sleep(realDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("-----------------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Новый файл будет называться " + file.getName());
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.out.println("Незаданы все необходимые параметры. Их должно быть не меньше двух.");
        } else {
            var validator = new FileDownloadParamsValidator(args);
            if (validator.urlValidator() && validator.speedValidator()) {
                var parameters = FileDownloadParamsValidator.getResultMap();
                int speed = Integer.parseInt(parameters.get("speed"));
                Thread wget = new Thread(new Wget(parameters.get("url"), speed));
                wget.start();
                wget.join();
            }
        }
    }
}
