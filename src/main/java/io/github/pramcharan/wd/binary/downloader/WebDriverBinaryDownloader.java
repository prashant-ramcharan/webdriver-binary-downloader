package io.github.pramcharan.wd.binary.downloader;

import io.github.pramcharan.wd.binary.downloader.download.ChromeDownloadProperties;
import io.github.pramcharan.wd.binary.downloader.download.DownloadProperties;
import io.github.pramcharan.wd.binary.downloader.download.GeckoDownloadProperties;
import io.github.pramcharan.wd.binary.downloader.enums.BrowserType;
import io.github.pramcharan.wd.binary.downloader.exception.WebDriverBinaryDownloaderException;
import io.github.pramcharan.wd.binary.downloader.utils.BinaryDownloadUtils;
import io.github.pramcharan.wd.binary.downloader.utils.DecompressFileUtils;
import io.github.pramcharan.wd.binary.downloader.utils.TempFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;

import static java.lang.System.out;
import static java.lang.System.setProperty;

public class WebDriverBinaryDownloader {
    private boolean strictDownload;
    private DownloadProperties downloadProperties;
    private File binaryDownloadDirectory;

    private WebDriverBinaryDownloader() {
        createBinaryDownloadDirectory();
    }

    public static WebDriverBinaryDownloader create() {
        return new WebDriverBinaryDownloader();
    }

    public void downloadLatestBinaryAndConfigure(BrowserType browserType) {
        createDownloadProperties.accept(browserType, null);
        downloadBinaryAndConfigure(browserType);
    }

    public void downloadBinaryAndConfigure(BrowserType browserType, String release) {
        createDownloadProperties.accept(browserType, release);
        downloadBinaryAndConfigure(browserType);
    }

    public WebDriverBinaryDownloader strictDownload() {
        this.strictDownload = true;
        return this;
    }

    private BiConsumer<BrowserType, String> createDownloadProperties = ((browserType, release) -> {
        switch (browserType) {
            case CHROME:
                this.downloadProperties = release == null ? ChromeDownloadProperties.forLatestRelease() : ChromeDownloadProperties.forPreviousRelease(release);
                break;

            case FIREFOX:
                this.downloadProperties = release == null ? GeckoDownloadProperties.forLatestRelease() : GeckoDownloadProperties.forPreviousRelease(release);
                break;
        }
    });

    private WebDriverBinaryDownloader downloadBinaryAndConfigure(BrowserType browserType) {
        if (!strictDownload && getWebDriverBinary().exists()) {
            out.println("Re-using an existing driver binary found at: " + getWebDriverBinary().getParent());
        } else {
            BinaryDownloadUtils.downloadBinary(downloadProperties.getDownloadURL(), downloadProperties.getCompressedBinaryFile());

            out.println(String.format("%s successfully downloaded to: %s", downloadProperties.getBinaryDriverName(), getWebDriverBinary().getParent()));

            decompressBinary();
        }
        configureBinary(browserType);
        return this;
    }

    private File decompressBinary() {
        final File decompressedBinary = DecompressFileUtils.decompressFile(
                downloadProperties.getCompressedBinaryFile(),
                new File(binaryDownloadDirectory + File.separator + downloadProperties.getBinaryDirectory()),
                downloadProperties.getCompressedBinaryType());

        switch (downloadProperties.getBinaryEnvironment().getOsType()) {
            case MAC:
            case LINUX:
                try {
                    Runtime.getRuntime().exec("chmod u+x " + decompressedBinary.getAbsolutePath()).waitFor();
                } catch (IOException | InterruptedException e) {
                    throw new WebDriverBinaryDownloaderException(e);
                }
        }
        return decompressedBinary;
    }

    private void configureBinary(BrowserType browserType) {
        switch (browserType) {
            case CHROME: {
                setProperty("webdriver.chrome.driver", getWebDriverBinary().getAbsolutePath());
                break;
            }
            case FIREFOX: {
                setProperty("webdriver.gecko.driver", getWebDriverBinary().getAbsolutePath());
                break;
            }
        }
    }

    private void createBinaryDownloadDirectory() {
        binaryDownloadDirectory = new File(TempFileUtils.getTempDirectory() + "webdriver_binaries" + File.separator);

        if (!binaryDownloadDirectory.exists()) {
            binaryDownloadDirectory.mkdir();
        }
    }

    private File getWebDriverBinary() {
        return new File(binaryDownloadDirectory.getAbsolutePath() +
                File.separator +
                downloadProperties.getBinaryDirectory() +
                File.separator +
                downloadProperties.getBinaryName());
    }
}