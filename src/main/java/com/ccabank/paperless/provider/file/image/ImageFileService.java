package com.ccabank.paperless.provider.file.image;

import com.ccabank.paperless.provider.file.FileConstant;
import com.ccabank.paperless.provider.file.FileService;
import com.ccabank.paperless.provider.file.MediaFile;
import com.ccabank.paperless.provider.file.UnsupportedFileTypeException;
import com.ccabank.paperless.provider.file.util.DateUtil;
import com.ccabank.paperless.provider.file.util.FileUtil;
import com.ccabank.paperless.provider.file.util.StringUtil;
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

@Service
public final class ImageFileService implements FileService {

    private final String imageExtensionSave = ".jpg";

    private final String[] mimeTypeSupport = {MimeTypeUtils.IMAGE_JPEG_VALUE,
            MimeTypeUtils.IMAGE_GIF_VALUE, MimeTypeUtils.IMAGE_PNG_VALUE};


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

    @Override
    public Boolean remove(String pathFile) {
        return FileUtil.deleteFile(pathFile);
    }
}
