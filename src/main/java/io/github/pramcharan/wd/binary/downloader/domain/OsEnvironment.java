package io.github.pramcharan.wd.binary.downloader.domain;

import io.github.pramcharan.wd.binary.downloader.enums.OsType;
import io.github.pramcharan.wd.binary.downloader.utils.OsEnvironmentUtils;

public class OsEnvironment {
    private int archType;

    private OsEnvironment() {
    }

    private OsEnvironment(int archType) {
        this.archType = archType;
    }

    public static OsEnvironment create() {
        return new OsEnvironment();
    }

    public static OsEnvironment create(int archType) {
        return new OsEnvironment(archType);
    }

    public OsType getOsType() {
        return OsEnvironmentUtils.getOsType();
    }

    public int getArchitecture() {
        return archType != 0 ? archType : OsEnvironmentUtils.getOsArch();
    }

    public String getOsNameAndArch() {
        return (getOsType().name() + getArchitecture()).toLowerCase();
    }
}