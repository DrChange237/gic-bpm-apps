package com.ccabank.memoservice.provider.file.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : entity-service
 * @Package : com.ccabank.userservice.provider.file.util
 * <p>
 * @date: 19/06/2023
 * @time: 09:52
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
public class FileUtil {


    /**
     * This function attempts to delete a file at a specified path and returns a boolean indicating
     * whether the deletion was successful or not.
     *
     * @param pathFile The path of the file that needs to be deleted.
     * @return The method is returning a Boolean value, either true or false, depending on whether the
     * file at the specified path was successfully deleted or not.
     */
    public static Boolean deleteFile(String pathFile) {
        try {
            Files.deleteIfExists(Paths.get(pathFile));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This Java function returns the file extension of a given file name.
     *
     * @param fileName The parameter "fileName" is a String variable that represents the name of a
     *                 file, including its extension.
     * @return The method `getFileExtension` returns the file extension of a given file name as a
     * `String`. If the file name does not have an extension, an empty string is returned.
     */
    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }
}
