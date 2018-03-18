package io.github.pramcharan.wd.binary.downloader.download;

import io.github.pramcharan.wd.binary.downloader.domain.OsEnvironment;
import io.github.pramcharan.wd.binary.downloader.enums.CompressedBinaryType;

import java.io.File;
import java.net.URL;

public interface DownloadProperties {
    URL getDownloadURL();

    OsEnvironment getBinaryEnvironment();

    File getCompressedBinaryFile();

    CompressedBinaryType getCompressedBinaryType();

    String getBinaryName();

    String getBinaryDirectory();

    String getBinaryDriverName();
}