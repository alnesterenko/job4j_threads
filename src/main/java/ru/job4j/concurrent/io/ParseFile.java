package ru.job4j.concurrent.io;

import java.io.*;
import java.util.function.Predicate;

public class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    private synchronized String gettingContent(Predicate<Character> predicate) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedInputStream input = new BufferedInputStream(
                new FileInputStream(file))) {
            int data;
            while ((data = input.read()) != -1) {
                char oneSymbol = (char) data;
                if (predicate.test(oneSymbol)) {
                    stringBuilder.append((char) data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public String getContent() {
        Predicate<Character> predicate = symbol -> true;
        return gettingContent(predicate);
    }

    public String getContentWithoutUnicode() {
        Predicate<Character> predicate = symbol -> symbol < 0x80;
        return gettingContent(predicate);
    }
}
