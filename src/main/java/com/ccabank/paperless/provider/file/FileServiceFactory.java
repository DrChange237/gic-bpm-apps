package com.ccabank.paperless.provider.file;

import com.ccabank.paperless.provider.file.image.ImageFileService;
import org.springframework.stereotype.Component;

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
@Component
public class FileServiceFactory {

    /**
     * Construct
     */
    private FileServiceFactory() {
    }

    /**
     * The function returns a specific type of file service based on the input file type.
     *
     * @param fileType The parameter `fileType` is of type `FileType`, which is an enum that
     *                 represents different types of files. In this code snippet, it is used to
     *                 determine which implementation of the `FileService` interface to return. If the
     *                 `fileType` is `IMAGE`, it returns an instance of
     * @return A `FileService` object is being returned based on the `fileType` parameter passed to
     * the method. If the `fileType` is `IMAGE`, an `ImageFileService` object is returned. If the
     * `fileType` is any other value, an `IllegalArgumentException` is thrown with the message "This
     * FileType is unsupported!".
     */
    public final static ImageFileService getFileService(FileType fileType) {
        switch (fileType) {
            case IMAGE:
                return new ImageFileService();

            default:
                throw new IllegalArgumentException("This FileType is unsupported!");
        }
    }
}
