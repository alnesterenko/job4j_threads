package ru.job4j.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDownloadParamsValidator {

    private String[] params;
    private static Map<String, String> resultMap = new HashMap<>();

    public FileDownloadParamsValidator(String[] params) {
        this.params = params;
    }

    public static Map<String, String> getResultMap() {
        return resultMap;
    }

    public boolean urlValidator() {
        boolean result = false;
        String url = params[0].trim();
        try {
            new URL(url).toURI();

            if (additionalCheckUrl(url)) {
                resultMap.put("url", url);
                getFileNameAndExtension(url);
                result = true;
            } else {
                System.out.print("The URL " + url + " isn't valid");
            }
        } catch (URISyntaxException exception) {
            exception.printStackTrace();
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public boolean additionalCheckUrl(String url) {
        final Pattern URL_PATTERN = Pattern.compile("\\S+\\/\\/\\S+\\/\\S+\\.\\S{2,}$");
        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }

    public void getFileNameAndExtension(String url) {
        String[] wholeLine = url.split("/");
        String[] tempStrArr = wholeLine[wholeLine.length - 1].split("\\.");
        resultMap.put("filename", tempStrArr[0].trim());
        resultMap.put("extension", tempStrArr[1].trim());
    }

    public boolean speedValidator() {
        boolean result = false;
        String stringSpeed = params[1].trim();
        try {
            Integer.parseInt(stringSpeed);
            resultMap.put("speed", stringSpeed);
            result = true;
        } catch (NumberFormatException exception) {
            System.err.println("Неправильно указан параметр скорости!");
            exception.printStackTrace();
        }
        return result;
    }
}
