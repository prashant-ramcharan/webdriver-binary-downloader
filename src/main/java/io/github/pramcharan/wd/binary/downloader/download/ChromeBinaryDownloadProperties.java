package io.github.pramcharan.wd.binary.downloader.download;

import io.github.pramcharan.wd.binary.downloader.domain.OsEnvironment;
import io.github.pramcharan.wd.binary.downloader.domain.URLLookup;
import io.github.pramcharan.wd.binary.downloader.enums.TargetArch;
import io.github.pramcharan.wd.binary.downloader.enums.CompressedBinaryType;
import io.github.pramcharan.wd.binary.downloader.enums.OsType;
import io.github.pramcharan.wd.binary.downloader.exception.WebDriverBinaryDownloaderException;
import io.github.pramcharan.wd.binary.downloader.utils.BinaryDownloadUtils;
import io.github.pramcharan.wd.binary.downloader.utils.TempFileUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ChromeBinaryDownloadProperties implements BinaryDownloadProperties {
    private String release;
    private TargetArch targetArch;

    private final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/chromedriver_%s.zip";

    private ChromeBinaryDownloadProperties() {
        release = getLatestRelease();

        if (release.length() == 0) {
            throw new WebDriverBinaryDownloaderException("Unable to read the latest ChromeDriver release from: " + URLLookup.CHROMEDRIVER_LATEST_RELEASE_URL);
        }
    }

    private ChromeBinaryDownloadProperties(String release) {
        this.release = release;
    }

    public static ChromeBinaryDownloadProperties forLatestRelease() {
        return new ChromeBinaryDownloadProperties();
    }

    public static ChromeBinaryDownloadProperties forPreviousRelease(String release) {
        return new ChromeBinaryDownloadProperties(release);
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

        return targetArch != null
                ? OsEnvironment.create(targetArch.getValue())
                : osEnvironment.getOsType().equals(OsType.WIN)
                ? OsEnvironment.create(32)
                : osEnvironment;
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
    public String getBinaryFilename() {
        return getBinaryEnvironment().getOsType().equals(OsType.WIN) ? "chromedriver.exe" : "chromedriver";
    }

    public String getBinaryDirectory() {
        return release != null ? "chromedriver_" + release : "chromedriver";
    }

    @Override
    public String getBinaryDriverName() {
        return "ChromeDriver";
    }

    @Override
    public String getBinaryVersion() {
        return release;
    }

    @Override
    public void setBinaryArchitecture(TargetArch targetArch) {
        this.targetArch = targetArch;
    }

    private String getLatestRelease() {
        try {
            return BinaryDownloadUtils.downloadAndReadFile(new URL(URLLookup.CHROMEDRIVER_LATEST_RELEASE_URL));
        } catch (MalformedURLException e) {
            throw new WebDriverBinaryDownloaderException(e);
        }
    }
}