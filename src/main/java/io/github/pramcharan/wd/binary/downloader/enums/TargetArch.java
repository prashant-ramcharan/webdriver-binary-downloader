package io.github.pramcharan.wd.binary.downloader.enums;

public enum TargetArch {
    X86(32),
    X64(64);

    int value;

    TargetArch(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}