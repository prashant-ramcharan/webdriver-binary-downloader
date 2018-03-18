package io.github.pramcharan.wd.binary.downloader.utils;

import io.github.pramcharan.wd.binary.downloader.enums.OsType;

import java.util.function.BiPredicate;
import java.util.function.Function;

public final class OsEnvironmentUtils {

    public static OsType getOsType() {
        final String osName = System.getProperty("os.name");
        return osNameTest.test(osName, "windows") ? OsType.WIN : osNameTest.test(osName, "mac") ? OsType.MAC : OsType.LINUX;
    }

    public static int getOsArch() {
        final String osArch = System.getProperty("os.arch");
        return osArchFunc.apply(osArch.toLowerCase());
    }

    private static BiPredicate<String, String> osNameTest = (osName, criteria) -> osName.toLowerCase().contains(criteria);

    private static Function<String, Integer> osArchFunc = (osArch) -> (osArch.contains("86") && !osArch.contains("64")) ? 32 : 64;
}