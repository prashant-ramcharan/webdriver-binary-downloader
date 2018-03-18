package io.github.pramcharan.wd.binary.downloader.domain;

public class DownloadResult {
    private String binaryName;
    private String binaryVersion;
    private String binaryLocation;

    public DownloadResult(String binaryName, String binaryVersion, String binaryLocation) {
        this.binaryName = binaryName;
        this.binaryVersion = binaryVersion;
        this.binaryLocation = binaryLocation;
    }

    public String getBinaryName() {
        return binaryName;
    }

    public String getBinaryVersion() {
        return binaryVersion;
    }

    public String getBinaryLocation() {
        return binaryLocation;
    }
}