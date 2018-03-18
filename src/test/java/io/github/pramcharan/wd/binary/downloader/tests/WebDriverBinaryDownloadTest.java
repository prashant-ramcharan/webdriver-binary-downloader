package io.github.pramcharan.wd.binary.downloader.tests;

import io.github.pramcharan.wd.binary.downloader.WebDriverBinaryDownloader;
import io.github.pramcharan.wd.binary.downloader.domain.DownloadResult;
import io.github.pramcharan.wd.binary.downloader.enums.BrowserType;
import org.junit.Assert;
import org.junit.Test;

public class WebDriverBinaryDownloadTest {

    @Test
    public void downloadLatestChromeDriverBinary() {
        final DownloadResult result = WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(BrowserType.CHROME);
        Assert.assertNotNull(result.getBinaryLocation());
    }

    @Test
    public void downloadLatestGeckoDriverBinary() {
        final DownloadResult result = WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(BrowserType.FIREFOX);
        Assert.assertNotNull(result.getBinaryLocation());
    }

    @Test
    public void downloadLatestIEDriverBinary() {
        final DownloadResult result = WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(BrowserType.IEXPLORER);
        Assert.assertNotNull(result.getBinaryLocation());
    }
}