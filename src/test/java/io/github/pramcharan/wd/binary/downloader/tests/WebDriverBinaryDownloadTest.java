package io.github.pramcharan.wd.binary.downloader.tests;

import io.github.pramcharan.wd.binary.downloader.WebDriverBinaryDownloader;
import io.github.pramcharan.wd.binary.downloader.domain.DownloadResult;
import io.github.pramcharan.wd.binary.downloader.enums.TargetArch;
import io.github.pramcharan.wd.binary.downloader.enums.BrowserType;
import org.junit.Assert;
import org.junit.Test;

public class WebDriverBinaryDownloadTest {

    @Test
    public void downloadLatestChromeDriverBinary() {
        final DownloadResult downloadResult = WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(BrowserType.CHROME);
        Assert.assertNotNull(downloadResult.getBinaryLocation());
    }

    @Test
    public void downloadLatestGeckoDriverBinary() {
        final DownloadResult downloadResult = WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(BrowserType.FIREFOX);
        Assert.assertNotNull(downloadResult.getBinaryLocation());
    }

    @Test
    public void downloadLatestIEDriverBinary() {
        final DownloadResult downloadResult = WebDriverBinaryDownloader.create().downloadLatestBinaryAndConfigure(BrowserType.IEXPLORER);
        Assert.assertNotNull(downloadResult.getBinaryLocation());
    }

    @Test
    public void downloadOlderChromeDriverBinary() {
        final DownloadResult downloadResult = WebDriverBinaryDownloader.create().downloadBinaryAndConfigure(BrowserType.CHROME, "2.35");
        Assert.assertNotNull(downloadResult.getBinaryLocation());
    }

    @Test
    public void downloadOlderGeckoDriverBinary() {
        final DownloadResult downloadResult = WebDriverBinaryDownloader.create().downloadBinaryAndConfigure(BrowserType.FIREFOX, "v0.19.0");
        Assert.assertNotNull(downloadResult.getBinaryLocation());
    }

    @Test
    public void downloadOlderIEDriverBinary() {
        final DownloadResult downloadResult = WebDriverBinaryDownloader.create().downloadBinaryAndConfigure(BrowserType.IEXPLORER, "3.8");
        Assert.assertNotNull(downloadResult.getBinaryLocation());
    }

    @Test
    public void downloadIEDriverBinaryWithArchitecture() {
        final DownloadResult downloadResult = WebDriverBinaryDownloader.create().strictDownload().downloadBinaryAndConfigure(BrowserType.IEXPLORER, "3.7", TargetArch.X86);
        Assert.assertNotNull(downloadResult.getBinaryLocation());
    }
}