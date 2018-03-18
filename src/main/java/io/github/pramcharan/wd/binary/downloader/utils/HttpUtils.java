package io.github.pramcharan.wd.binary.downloader.utils;

import io.github.pramcharan.wd.binary.downloader.exception.WebDriverBinaryDownloaderException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.function.Function;

public final class HttpUtils {

    public static String getResponseContent(String endpoint) {
        try {
            return new BufferedReader(new InputStreamReader(getResponseStream.apply(endpoint))).readLine();
        } catch (IOException e) {
            throw new WebDriverBinaryDownloaderException(e);
        }
    }

    public static InputStream getResponseInputStream(String endpoint) {
        return getResponseStream.apply(endpoint);
    }

    private static Function<String, InputStream> getResponseStream = (endpoint) -> {
        final HttpURLConnection connection;

        try {
            URL url = new URL(endpoint);
            connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return connection.getInputStream();
            }
        } catch (Exception e) {
            throw new WebDriverBinaryDownloaderException(e);
        }
        throw new WebDriverBinaryDownloaderException("Unable to get the Http response input stream. Response headers -> " + Arrays.toString(connection.getHeaderFields().values().toArray()));
    };
}
