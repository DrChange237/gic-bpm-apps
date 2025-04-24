package com.ccabank.paperless.provider.file;

import java.io.File;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : entity-service
 * @Package : com.ccabank.userservice.provider.file
 * <p>
 * @date: 19/06/2023
 * @time: 09:56
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
public class MediaFile {


    // `String pathFolder;` is declaring a private instance variable `pathFolder` of type `String` in the
    // `MediaFile` class. This variable is used to store the path of the folder where the media file is
    // located.
    String pathFolder;


    // `String pathUrl;` is declaring a private instance variable `pathUrl` of type `String` in the
    // `MediaFile` class. This variable is used to store the URL path of the media file.
    String pathUrl;

    /**
     * contructeur par defaut
     */
    public MediaFile() {
    }

    /**
     * contructeur avec param
     *
     * @param pathFolder
     * @param pathUrl
     */
    public MediaFile(String pathFolder, String pathUrl) {
        this.pathFolder = pathFolder;
        this.pathUrl = pathUrl;
    }

    /**
     * The function returns the path folder as a string.
     *
     * @return The method is returning a String value which is the pathFolder.
     */
    public String getPathFolder() {
        return pathFolder;
    }

    /**
     * This function sets the path folder for a Java object.
     *
     * @param pathFolder pathFolder is a variable of type String that represents the path to a folder
     *                   in a file system. This method sets the value of the pathFolder variable to
     *                   the value passed as a parameter.
     */
    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }

    /**
     * The function returns the value of the pathUrl variable as a string.
     *
     * @return The method is returning a String value which is the value of the variable `pathUrl`.
     */
    public String getPathUrl() {
        return pathUrl;
    }

    /**
     * This function sets the value of the "pathUrl" variable.
     *
     * @param pathUrl The parameter `pathUrl` is a string that represents the URL path for a resource.
     *                This method sets the value of the `pathUrl` instance variable to the value
     *                passed as a parameter.
     */
    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    /**
     * The function returns a File object representing the pathFolder.
     *
     * @return A `File` object is being returned. The `File` object represents a file or directory on
     * the file system, and it is created using the `pathFolder` string that is passed to the
     * constructor.
     */
    public File getFile() {
        return new File(pathFolder);
    }
}
