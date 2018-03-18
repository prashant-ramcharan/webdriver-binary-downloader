package io.github.pramcharan.wd.binary.downloader.utils;

import io.github.pramcharan.wd.binary.downloader.enums.CompressedBinaryType;
import io.github.pramcharan.wd.binary.downloader.exception.WebDriverBinaryDownloaderException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class DecompressFileUtils {

    public static File decompressFile(File source, File destination, CompressedBinaryType compressedBinaryType) {
        createDestinationDirectory(destination);

        return compressedBinaryType.equals(CompressedBinaryType.ZIP) ? unzip(source, destination) : tar(source, destination);
    }

    private static File unzip(File source, File destination) {
        File decompressedFile = null;

        try {
            byte[] buffer = new byte[1024];

            final ZipInputStream inputStream = new ZipInputStream(new FileInputStream(source.toString()));

            ZipEntry entry = inputStream.getNextEntry();

            while (entry != null) {
                String fileName = entry.getName();
                decompressedFile = new File(destination.getAbsolutePath() + File.separator + fileName);

                FileOutputStream fos = new FileOutputStream(decompressedFile);
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                entry = inputStream.getNextEntry();
            }
            inputStream.closeEntry();
            inputStream.close();
        } catch (IOException ex) {
            throw new WebDriverBinaryDownloaderException(ex);
        }
        return decompressedFile;
    }

    private static File tar(File source, File destination) {
        try {
            Runtime.getRuntime().exec(String.format("tar -C %s -zxvf %s", destination.getAbsolutePath(), source.getAbsolutePath()));
        } catch (IOException e) {
            throw new WebDriverBinaryDownloaderException(e);
        }
        return destination;
    }

    private static void createDestinationDirectory(File destination) {
        if (!destination.exists()) {
            destination.mkdir();
        }
    }
}
