package io.github.pramcharan.wd.binary.downloader.utils;

import io.github.pramcharan.wd.binary.downloader.exception.WebDriverBinaryDownloaderException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public final class BinaryDownloadUtils {

    public static void downloadBinary(URL downloadURL, File downloadTo) {
        download(downloadURL, downloadTo, false);
    }

    public static String downloadAndReadFile(URL downloadURL) {
        File destinationFile = TempFileUtils.createTempFileAndDeleteOnExit();

        download(downloadURL, destinationFile, true);

        if (destinationFile.exists()) {
            try {
                return FileUtils.readFileToString(destinationFile, Charset.defaultCharset()).trim();
            } catch (IOException e) {
                throw new WebDriverBinaryDownloaderException(e);
            }
        }
        throw new WebDriverBinaryDownloaderException("Unable to download file from: " + getAbsoluteURL(downloadURL));
    }

    private static void download(URL downloadURL, File downloadTo, boolean silentDownload) {
        try {
            long downloadStartTime = System.nanoTime();

            if (!silentDownload) {
                out.print("Downloading driver binary from: " + getAbsoluteURL(downloadURL));
            }

            FileUtils.copyURLToFile(downloadURL, downloadTo);

            long downloadEndTime = System.nanoTime() - downloadStartTime;

            if (!silentDownload) {
                out.println(" -> " + String.format("%d min, %d sec", TimeUnit.NANOSECONDS.toHours(downloadEndTime), TimeUnit.NANOSECONDS.toSeconds(downloadEndTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(downloadEndTime))));
            }
        } catch (IOException e) {
            throw new WebDriverBinaryDownloaderException(e);
        }
    }

    private static String getAbsoluteURL(URL url) {
        return url.getProtocol() + "://" + url.getHost() + url.getPath();
    }
}