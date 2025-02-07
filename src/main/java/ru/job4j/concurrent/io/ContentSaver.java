package ru.job4j.concurrent.io;

import java.io.*;

public class ContentSaver {
    private final File file;

    public ContentSaver(File file) {
        this.file = file;
    }

    public synchronized void saveContent(String content) {
        try (BufferedOutputStream output = new BufferedOutputStream(
                new FileOutputStream(file))) {
            output.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
