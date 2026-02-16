package com.change.gic.provider.file.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class FileUtil {

    public static Boolean deleteFile(String pathFile) {
        try {
            Files.deleteIfExists(Paths.get(pathFile));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
