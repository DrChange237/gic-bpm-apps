package com.ccabank.paperless.provider.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : entity-service
 * @Package : com.ccabank.userservice.provider.file
 * <p>
 * @date: 19/06/2023
 * @time: 09:55
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
public interface FileService {

    /**
     * This function uploads a media file with a given file name and file type, and returns the
     * uploaded media file.
     *
     * @param fileName The name of the file being uploaded.
     * @param file     file is an object of type MultipartFile which represents a file uploaded
     *                 through a web form. It contains the contents of the file as well as metadata
     *                 such as the file name, content type, and size.
     * @return The method `upload` is returning an object of type `MediaFile`.
     */
    MediaFile upload(String fileName, MultipartFile file)
            throws IOException, UnsupportedFileTypeException;

    /**
     * The function "remove" takes a file path as input and returns a boolean indicating whether the
     * file was successfully removed or not.
     *
     * @param pathFile The path to the file that needs to be removed. It is a string that specifies
     *                 the location of the file in the file system.
     * @return A boolean value is being returned. The method is likely attempting to remove a file at
     * the specified path, and the boolean value indicates whether the operation was successful (true)
     * or not (false).
     */
    Boolean remove(String pathFile);

}
