package io.github.pramcharan.wd.binary.downloader.download;

import io.github.pramcharan.wd.binary.downloader.domain.OsEnvironment;
import io.github.pramcharan.wd.binary.downloader.domain.URLLookup;
import io.github.pramcharan.wd.binary.downloader.enums.TargetArch;
import io.github.pramcharan.wd.binary.downloader.enums.CompressedBinaryType;
import io.github.pramcharan.wd.binary.downloader.exception.WebDriverBinaryDownloaderException;
import io.github.pramcharan.wd.binary.downloader.utils.HttpUtils;
import io.github.pramcharan.wd.binary.downloader.utils.TempFileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

public class IExplorerBinaryDownloadProperties implements BinaryDownloadProperties {
    private String release;
    private TargetArch targetArch;

    private final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/IEDriverServer_%s_%s.0.zip";

    private IExplorerBinaryDownloadProperties() {
        release = getLatestRelease();

        if (release.length() == 0) {
            throw new WebDriverBinaryDownloaderException("Unable to read the latest IEDriver release from: " + URLLookup.IEDRIVER_LATEST_RELEASE_URL);
        }
    }

    private IExplorerBinaryDownloadProperties(String release) {
        this.release = release;
    }

    public static IExplorerBinaryDownloadProperties forLatestRelease() {
        return new IExplorerBinaryDownloadProperties();
    }

    public static IExplorerBinaryDownloadProperties forPreviousRelease(String release) {
        return new IExplorerBinaryDownloadProperties(release);
    }

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                    BINARY_DOWNLOAD_URL_PATTERN,
                    URLLookup.IEDRIVER_URL,
                    release,
                    osArc.apply(getBinaryEnvironment()),
                    release));

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryDownloaderException(e);
        }
    }

    private Function<OsEnvironment, String> osArc = (osEnvironment) -> osEnvironment.getArchitecture() == 32 ? "Win32" : "x64";

    @Override
    public OsEnvironment getBinaryEnvironment() {
        return targetArch != null ? OsEnvironment.create(targetArch.getValue()) : OsEnvironment.create();
    }

    @Override
    public File getCompressedBinaryFile() {
        return new File(String.format("%s/iedriver_%s.zip", TempFileUtils.getTempDirectory(), release));
    }

    @Override
    public CompressedBinaryType getCompressedBinaryType() {
        return CompressedBinaryType.ZIP;
    }

    @Override
    public String getBinaryFilename() {
        return "IEDriverServer.exe";
    }

    public String getBinaryDirectory() {
        return release != null ? "iedriver_" + release : "iedriver";
    }

    @Override
    public String getBinaryDriverName() {
        return "IEDriver";
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
        final InputStream downloadStream = HttpUtils.getResponseInputStream(URLLookup.IEDRIVER_LATEST_RELEASE_URL);

        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(downloadStream);
            doc.getDocumentElement().normalize();

            final XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            Element element = (Element) xpath.evaluate("(//Contents/Key[contains(.,'/IEDriverServer')])[last()]", doc, XPathConstants.NODE);

            if (element == null) {
                return "";
            }

            final String release = element.getTextContent();
            return release.substring(0, release.indexOf("/"));

        } catch (Exception e) {
            throw new WebDriverBinaryDownloaderException(e);
        }
    }
}