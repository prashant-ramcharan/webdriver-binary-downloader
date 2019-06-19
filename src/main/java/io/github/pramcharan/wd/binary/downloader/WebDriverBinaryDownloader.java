package io.github.pramcharan.wd.binary.downloader;

import io.github.pramcharan.wd.binary.downloader.domain.DownloadResult;
import io.github.pramcharan.wd.binary.downloader.download.BinaryDownloadProperties;
import io.github.pramcharan.wd.binary.downloader.download.ChromeBinaryDownloadProperties;
import io.github.pramcharan.wd.binary.downloader.download.GeckoBinaryDownloadProperties;
import io.github.pramcharan.wd.binary.downloader.download.IExplorerBinaryDownloadProperties;
import io.github.pramcharan.wd.binary.downloader.enums.Browser;
import io.github.pramcharan.wd.binary.downloader.enums.TargetArch;
import io.github.pramcharan.wd.binary.downloader.exception.WebDriverBinaryDownloaderException;
import io.github.pramcharan.wd.binary.downloader.utils.BinaryDownloadUtils;
import io.github.pramcharan.wd.binary.downloader.utils.DecompressFileUtils;
import io.github.pramcharan.wd.binary.downloader.utils.TempFileUtils;

import java.io.File;
import java.io.IOException;

import static java.lang.System.out;
import static java.lang.System.setProperty;

public class WebDriverBinaryDownloader {
    private BinaryDownloadProperties binaryDownloadProperties;
    private File binaryDownloadDirectory;

    private boolean strictDownload;

    private WebDriverBinaryDownloader(String downloadLocation) {
        binaryDownloadDirectory = new File(downloadLocation + File.separator + "webdriver_binaries" + File.separator);

        if (!binaryDownloadDirectory.exists()) {
            binaryDownloadDirectory.mkdirs();
        }
    }

    public static WebDriverBinaryDownloader create() {
        return new WebDriverBinaryDownloader(TempFileUtils.getTempDirectory());
    }

    public static WebDriverBinaryDownloader createIn(String downloadLocation) {
        return new WebDriverBinaryDownloader(downloadLocation);
    }

    public DownloadResult downloadLatestBinaryAndConfigure(Browser browser) {
        return withDownloadProperties(browser, null, null).downloadBinaryAndConfigure(browser);
    }

    public DownloadResult downloadLatestBinaryAndConfigure(Browser browser, TargetArch targetArch) {
        return withDownloadProperties(browser, null, targetArch).downloadBinaryAndConfigure(browser);
    }

    public DownloadResult downloadBinaryAndConfigure(Browser browser, String release) {
        return withDownloadProperties(browser, release, null).downloadBinaryAndConfigure(browser);
    }

    public DownloadResult downloadBinaryAndConfigure(Browser browser, String release, TargetArch targetArch) {
        return withDownloadProperties(browser, release, targetArch).downloadBinaryAndConfigure(browser);
    }

    public WebDriverBinaryDownloader strictDownload() {
        this.strictDownload = true;
        return this;
    }

    private WebDriverBinaryDownloader withDownloadProperties(Browser browser, String release, TargetArch targetArch) {
        switch (browser) {
            case CHROME:
                this.binaryDownloadProperties = release == null ? ChromeBinaryDownloadProperties.forLatestRelease() : ChromeBinaryDownloadProperties.forPreviousRelease(release);
                break;

            case FIREFOX:
                this.binaryDownloadProperties = release == null ? GeckoBinaryDownloadProperties.forLatestRelease() : GeckoBinaryDownloadProperties.forPreviousRelease(release);
                break;

            case IEXPLORER:
                this.binaryDownloadProperties = release == null ? IExplorerBinaryDownloadProperties.forLatestRelease() : IExplorerBinaryDownloadProperties.forPreviousRelease(release);
                break;
        }
        if (targetArch != null) {
            this.binaryDownloadProperties.setBinaryArchitecture(targetArch);
        }
        return this;
    }

    private DownloadResult downloadBinaryAndConfigure(Browser browser) {
        if (!strictDownload && getWebDriverBinary().exists()) {
            out.println("Re-using an existing driver binary found at: " + getWebDriverBinary().getParent());
        } else {
            BinaryDownloadUtils.downloadBinary(binaryDownloadProperties.getDownloadURL(), binaryDownloadProperties.getCompressedBinaryFile());

            out.println(String.format("%s successfully downloaded to: %s", binaryDownloadProperties.getBinaryDriverName(), getWebDriverBinary().getParent()));

            decompressBinary();
        }
        configureBinary(browser);

        return new DownloadResult(binaryDownloadProperties.getBinaryDriverName(), binaryDownloadProperties.getBinaryVersion(), getWebDriverBinary().getAbsolutePath());
    }

    private File decompressBinary() {
        final File decompressedBinary = DecompressFileUtils.decompressFile(
                binaryDownloadProperties.getCompressedBinaryFile(),
                new File(binaryDownloadDirectory + File.separator + binaryDownloadProperties.getBinaryDirectory()),
                binaryDownloadProperties.getCompressedBinaryType());

        switch (binaryDownloadProperties.getBinaryEnvironment().getOsType()) {
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

    private void configureBinary(Browser browser) {
        switch (browser) {
            case CHROME: {
                setProperty("webdriver.chrome.driver", getWebDriverBinary().getAbsolutePath());
                break;
            }
            case FIREFOX: {
                setProperty("webdriver.gecko.driver", getWebDriverBinary().getAbsolutePath());
                break;
            }
            case IEXPLORER: {
                setProperty("webdriver.ie.driver", getWebDriverBinary().getAbsolutePath());
                break;
            }
        }
    }

    private File getWebDriverBinary() {
        return new File(binaryDownloadDirectory.getAbsolutePath() +
                File.separator +
                binaryDownloadProperties.getBinaryDirectory() +
                File.separator +
                binaryDownloadProperties.getBinaryFilename());
    }
}