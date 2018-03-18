package io.github.pramcharan.wd.binary.downloader.utils;

import io.github.pramcharan.wd.binary.downloader.exception.WebDriverBinaryDownloaderException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Function;

public final class HttpUtils {

    public static String getLocation(String endpoint) {
        return extract(connection.apply(endpoint), t -> t.getHeaderField("Location"));
    }

    public static InputStream getResponseInputStream(String endpoint) {
        return extract(connection.apply(endpoint), t -> {
            try {
                return t.getInputStream();
            } catch (IOException e) {
                throw new WebDriverBinaryDownloaderException(e);
            }
        });
    }

    private static Function<String, HttpURLConnection> connection = (endpoint) -> {
        HttpURLConnection.setFollowRedirects(false);

        final HttpURLConnection connection;

        try {
            URL url = new URL(endpoint);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            return connection;
        } catch (Exception e) {
            throw new WebDriverBinaryDownloaderException(e);
        } finally {
            HttpURLConnection.setFollowRedirects(true);
        }
    };

    private static <T> T extract(HttpURLConnection connection, Function<HttpURLConnection, T> condition) {
        return condition.apply(connection);
    }
}