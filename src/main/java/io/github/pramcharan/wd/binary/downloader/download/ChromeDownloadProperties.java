package io.github.pramcharan.wd.binary.downloader.download;

import io.github.pramcharan.wd.binary.downloader.domain.OsEnvironment;
import io.github.pramcharan.wd.binary.downloader.domain.URLLookup;
import io.github.pramcharan.wd.binary.downloader.enums.CompressedBinaryType;
import io.github.pramcharan.wd.binary.downloader.enums.OsType;
import io.github.pramcharan.wd.binary.downloader.exception.WebDriverBinaryDownloaderException;
import io.github.pramcharan.wd.binary.downloader.utils.BinaryDownloadUtils;
import io.github.pramcharan.wd.binary.downloader.utils.TempFileUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ChromeDownloadProperties implements DownloadProperties {
    private String release;

    private final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/chromedriver_%s.zip";

    private ChromeDownloadProperties() {
        release = getLatestReleaseNo();

        if (release.length() == 0) {
            throw new WebDriverBinaryDownloaderException("Unable to read the latest ChromeDriver release from: " + URLLookup.CHROMEDRIVER_LATEST_RELEASE_URL);
        }
    }

    private ChromeDownloadProperties(String release) {
        this.release = release;
    }

    public static ChromeDownloadProperties forLatestRelease() {
        return new ChromeDownloadProperties();
    }

    public static ChromeDownloadProperties forPreviousRelease(String release) {
        return new ChromeDownloadProperties(release);
    }

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                    BINARY_DOWNLOAD_URL_PATTERN,
                    URLLookup.CHROMEDRIVER_URL,
                    release,
                    getBinaryEnvironment().getOsNameAndArch()));

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryDownloaderException(e);
        }
    }

    @Override
    public OsEnvironment getBinaryEnvironment() {
        final OsEnvironment osEnvironment = OsEnvironment.create();
        return osEnvironment.getOsType().equals(OsType.WIN) ? OsEnvironment.create(32) : osEnvironment;
    }

    @Override
    public File getCompressedBinaryFile() {
        return new File(String.format("%s/chromedriver_%s.zip", TempFileUtils.getTempDirectory(), release));
    }

    @Override
    public CompressedBinaryType getCompressedBinaryType() {
        return CompressedBinaryType.ZIP;
    }

    @Override
    public String getBinaryName() {
        return getBinaryEnvironment().getOsType().equals(OsType.WIN) ? "chromedriver.exe" : "chromedriver";
    }

    public String getBinaryDirectory() {
        return release != null ? "chromedriver_" + release : "chromedriver";
    }

    @Override
    public String getBinaryDriverName() {
        return "ChromeDriver";
    }

    private String getLatestReleaseNo() {
        try {
            return BinaryDownloadUtils.downloadAndReadFile(new URL(URLLookup.CHROMEDRIVER_LATEST_RELEASE_URL));
        } catch (MalformedURLException e) {
            throw new WebDriverBinaryDownloaderException(e);
        }
    }
}