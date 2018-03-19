package io.github.pramcharan.wd.binary.downloader.download;

import io.github.pramcharan.wd.binary.downloader.domain.OsEnvironment;
import io.github.pramcharan.wd.binary.downloader.enums.TargetArch;
import io.github.pramcharan.wd.binary.downloader.enums.CompressedBinaryType;

import java.io.File;
import java.net.URL;

public interface BinaryDownloadProperties {
    URL getDownloadURL();

    OsEnvironment getBinaryEnvironment();

    File getCompressedBinaryFile();

    CompressedBinaryType getCompressedBinaryType();

    String getBinaryFilename();

    String getBinaryDirectory();

    String getBinaryDriverName();

    String getBinaryVersion();

    void setBinaryArchitecture(TargetArch targetArch);
}