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

    @Override
    public void run() {
        int bufferSize = 512;
        long downloadedAt = 0;
        var parameters = FileDownloadParamsValidator.getResultMap();
        int speedLimit = Integer.parseInt(parameters.get("speed"));
        var file = new File(getNewNameOfFile() + "." + parameters.get("extension"));
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            var dataBuffer = new byte[bufferSize];
            int bytesRead;
            long timeStartDownloadPartOfFile = System.currentTimeMillis();
            int bytesAlreadyDownloaded = 0;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                output.write(dataBuffer, 0, bytesRead);
                bytesAlreadyDownloaded += bufferSize;
                if (bytesAlreadyDownloaded >= speedLimit) {
                    downloadedAt = System.currentTimeMillis() - timeStartDownloadPartOfFile;
                    if (downloadedAt < 1000) {
                        try {
                            Thread.sleep(1000 - downloadedAt);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    timeStartDownloadPartOfFile = System.currentTimeMillis();
                    bytesAlreadyDownloaded = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, IllegalArgumentException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Незаданы все необходимые параметры. Их должно быть не меньше двух!");
        }
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
