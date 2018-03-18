package io.github.pramcharan.wd.binary.downloader.utils;

import io.github.pramcharan.wd.binary.downloader.exception.WebDriverBinaryDownloaderException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class HttpUtils {

    public static String getHttpResponseContent(String endpoint) {
        try {
            URL url = new URL(endpoint);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
            }
        } catch (Exception e) {
            throw new WebDriverBinaryDownloaderException(e);
        }
        throw new WebDriverBinaryDownloaderException("Unable to get a successful response from: " + endpoint);
    }
}
