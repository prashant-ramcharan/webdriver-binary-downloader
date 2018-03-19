package io.github.pramcharan.wd.binary.downloader.download;

import io.github.pramcharan.wd.binary.downloader.domain.OsEnvironment;
import io.github.pramcharan.wd.binary.downloader.domain.URLLookup;
import io.github.pramcharan.wd.binary.downloader.enums.TargetArch;
import io.github.pramcharan.wd.binary.downloader.enums.CompressedBinaryType;
import io.github.pramcharan.wd.binary.downloader.enums.OsType;
import io.github.pramcharan.wd.binary.downloader.exception.WebDriverBinaryDownloaderException;
import io.github.pramcharan.wd.binary.downloader.utils.HttpUtils;
import io.github.pramcharan.wd.binary.downloader.utils.TempFileUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

public class GeckoBinaryDownloadProperties implements BinaryDownloadProperties {
    private String release;
    private TargetArch targetArch;

    private final String BINARY_DOWNLOAD_URL_TAR_PATTERN = "%s/%s/geckodriver-%s-%s.tar.gz";
    private final String BINARY_DOWNLOAD_URL_ZIP_PATTERN = "%s/%s/geckodriver-%s-%s.zip";

    private GeckoBinaryDownloadProperties() {
        release = getLatestRelease();

        if (release.length() == 0) {
            throw new WebDriverBinaryDownloaderException("Unable to read the latest GeckoDriver release from: " + URLLookup.GECKODRIVER_LATEST_RELEASE_URL);
        }
    }

    private GeckoBinaryDownloadProperties(String release) {
        this.release = release;
    }

    public static GeckoBinaryDownloadProperties forLatestRelease() {
        return new GeckoBinaryDownloadProperties();
    }

    public static GeckoBinaryDownloadProperties forPreviousRelease(String release) {
        return new GeckoBinaryDownloadProperties(release);
    }

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                    binaryDownloadPattern.apply(getBinaryEnvironment()),
                    URLLookup.GECKODRIVER_URL,
                    release,
                    release,
                    osNameAndArc.apply(getBinaryEnvironment())));

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryDownloaderException(e);
        }
    }

    private Function<OsEnvironment, String> binaryDownloadPattern = (osEnvironment) -> {
        if (osEnvironment.getOsType().equals(OsType.WIN)) {
            return BINARY_DOWNLOAD_URL_ZIP_PATTERN;
        } else {
            return BINARY_DOWNLOAD_URL_TAR_PATTERN;
        }
    };

    private Function<OsEnvironment, String> osNameAndArc = (osEnvironment) -> {
        if (osEnvironment.getOsType().equals(OsType.MAC)) {
            return "macos";
        } else {
            return osEnvironment.getOsNameAndArch();
        }
    };

    private Function<OsEnvironment, String> compressedBinaryExt = (osEnvironment) -> osEnvironment.getOsType().equals(OsType.WIN) ? "zip" : "tar.gz";

    @Override
    public OsEnvironment getBinaryEnvironment() {
        return targetArch != null ? OsEnvironment.create(targetArch.getValue()) : OsEnvironment.create();
    }

    @Override
    public File getCompressedBinaryFile() {
        return new File(String.format("%s/geckodriver_%s.%s",
                TempFileUtils.getTempDirectory(),
                release,
                compressedBinaryExt.apply(getBinaryEnvironment())));
    }

    @Override
    public CompressedBinaryType getCompressedBinaryType() {
        return getBinaryEnvironment().getOsType().equals(OsType.WIN) ? CompressedBinaryType.ZIP : CompressedBinaryType.TAR;
    }

    @Override
    public String getBinaryFilename() {
        return getBinaryEnvironment().getOsType().equals(OsType.WIN) ? "geckodriver.exe" : "geckodriver";
    }

    public String getBinaryDirectory() {
        return release != null ? "geckodriver_" + release : "geckodriver";
    }

    @Override
    public String getBinaryDriverName() {
        return "GeckoDriver";
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
        final String releaseLocation = HttpUtils.getLocation(URLLookup.GECKODRIVER_LATEST_RELEASE_URL);

        if (releaseLocation == null || releaseLocation.length() < 2 || !releaseLocation.contains("/")) {
            return "";
        }
        return releaseLocation.substring(releaseLocation.lastIndexOf("/") + 1);
    }
}