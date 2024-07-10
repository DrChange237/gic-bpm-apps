package com.ccabank.signservice.provider.file.image;

import com.ccabank.signservice.provider.file.FileConstant;
import com.ccabank.signservice.provider.file.FileService;
import com.ccabank.signservice.provider.file.MediaFile;
import com.ccabank.signservice.provider.file.UnsupportedFileTypeException;
import com.ccabank.signservice.provider.file.util.DateUtil;
import com.ccabank.signservice.provider.file.util.FileUtil;
import com.ccabank.signservice.provider.file.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : entity-service
 * @Package : com.ccabank.userservice.provider.file.image
 * <p>
 * @date: 19/06/2023
 * @time: 09:50
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
@Service
public final class ImageFileService implements FileService {


    // The `imageExtensionSave` variable is storing the file extension for the image files that will be
    // saved by this class. In this case, it is set to ".jpg", which means that all uploaded image files
    // will be saved with the ".jpg" extension.
    private final String imageExtensionSave = ".jpg";

    // The `mimeTypeSupport` variable is an array of strings that contains the MIME types of the image
    // files that are supported by this class. It includes the MIME types for JPEG, GIF, and PNG image
    // files. This array is used to check if the uploaded file is an image file and if it is supported by
    // this class. If the uploaded file is not an image file or if it is not supported by this class, an
    // `UnsupportedFileTypeException` is thrown.
    private final String[] mimeTypeSupport = {MimeTypeUtils.IMAGE_JPEG_VALUE,
            MimeTypeUtils.IMAGE_GIF_VALUE, MimeTypeUtils.IMAGE_PNG_VALUE};



    // The `upload` method in the `ImageFileService` class is used to upload an image file to the server.
    // It takes in two parameters: `fileName`, which is the name of the file to be uploaded, and `file`,
    // which is the actual image file to be uploaded.
    @Override
    public MediaFile upload(String fileName, MultipartFile file)
            throws IOException, UnsupportedFileTypeException {
        if (!Arrays.asList(mimeTypeSupport).contains(file.getContentType())) {
            throw new UnsupportedFileTypeException(
                    file.getOriginalFilename() + " is not an image file: [" + String.join("; ",
                            mimeTypeSupport) + "]");
        }
        Path imageFolder = Paths.get(FileConstant.IMAGE_FOLDER).toAbsolutePath().normalize();
        if (!Files.exists(imageFolder)) {
            Files.createDirectories(imageFolder);
        }
        fileName = StringUtil.normalizeUri(fileName) + "-" + DateUtil.GetCurrentTimeMillis()
                + imageExtensionSave;
        Files.deleteIfExists(Paths.get(imageFolder + fileName));
        Files.copy(file.getInputStream(), imageFolder.resolve(fileName), REPLACE_EXISTING);
        return new MediaFile(Paths.get(imageFolder.toString(), File.separator, fileName).toString(),
                FileConstant.USER_URL_PATH + fileName);
    }

    // The `remove` method in the `ImageFileService` class is used to delete a file from the server. It
    // takes in one parameter `pathFile`, which is the path of the file to be deleted. The method returns
    // a boolean value indicating whether the file was successfully deleted or not. The
    // `FileUtil.deleteFile` method is called to delete the file from the server.
    @Override
    public Boolean remove(String pathFile) {
        return FileUtil.deleteFile(pathFile);
    }
}
