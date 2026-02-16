package com.change.gic.modules.file.util;


import com.change.gic.modules.file.constant.FileConstant;
import com.change.gic.modules.file.exception.MaxFileSizeException;
import com.change.gic.modules.file.exception.UnsupportedFileTypeException;
import org.apache.commons.io.FileUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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

    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public static byte[] compressFile(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressFile(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

    public static boolean isFileSizeNotAllowed(MultipartFile file) {
        if(file.getContentType() == null)
            return true;

        if(file.getContentType().startsWith("image") && file.getSize() > FileConstant.MAX_IMAGE_FILE_SIZE)
            return true;

        if(file.getContentType().startsWith("video") && file.getSize() > FileConstant.MAX_VIDEO_FILE_SIZE)
            return true;

        return file.getContentType().startsWith("application") && file.getSize() > FileConstant.MAX_DOCUMENT_FILE_SIZE;
    }

    public static String uploadToTemp(String filename, MultipartFile file) throws IOException {
        Path path = Paths.get(FileUtils.getTempDirectory().getAbsolutePath(), UUID.randomUUID().toString());
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        String extension= getFileExtension(file.getOriginalFilename());
        Files.copy(
                file.getInputStream(),
                path.resolve(filename + "." + extension),
                REPLACE_EXISTING
        );

        return Paths.get(path.toString(), File.separator, filename + "." + extension).toString();
    }

    public static void uploadToFolder(String filename, MultipartFile file, String newPath) throws IOException {
        Path path = Paths.get(newPath).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            System.out.println("folder not exists");
            Files.createDirectories(path);
        }
        else{
            System.out.println("folder exists");
        }
        Files.copy(
                file.getInputStream(),
                path.resolve(filename),
                REPLACE_EXISTING
        );
    }

    public static String getIdFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static File getFileInFolder(String path) {
        System.out.println("getFileInFolder -> " + path);
        return new File(path);
    }

    public static void checkIfCanUpload(MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException {
        if(FileConstant.ALLOWED_EXTENSIONS.stream().noneMatch(extension -> extension.equalsIgnoreCase(FileUtil.getFileExtension(file.getOriginalFilename())))){
            throw new UnsupportedFileTypeException("Unsupported file type : " + FileUtil.getFileExtension(file.getOriginalFilename()));
        }

        if(file.getSize() > FileConstant.MAX_FILE_SIZE || FileUtil.isFileSizeNotAllowed(file)){
            throw new MaxFileSizeException("File size exceeds maximum allowed");
        }
    }

    public static com.change.gic.modules.file.entity.File buildFileToSaveToTemp(String project, MultipartFile file)
            throws IOException {
        com.change.gic.modules.file.entity.File fileToSave = new com.change.gic.modules.file.entity.File();
        fileToSave.setId(UUID.randomUUID().toString());
        fileToSave.setName(UUID.randomUUID().toString());
        fileToSave.setUrl("");
        fileToSave.setSize(file.getSize());
        fileToSave.setType(file.getContentType());
        fileToSave.setProject(project);
        fileToSave.setIsInFolder(true);
        fileToSave.setIsTemp(true);

        String path;
        try{
            path = uploadToTemp(fileToSave.getId(), file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Could not upload file " + file.getOriginalFilename() + " to " + project + " folder \n:" + e.getMessage());
        }

        fileToSave.setPath(path);
        return fileToSave;
    }

    public static com.change.gic.modules.file.entity.File buildFileToSaveToFolder(String project, String pathStr, MultipartFile file, String baseFolder)
            throws IOException {
        com.change.gic.modules.file.entity.File fileToSave = new com.change.gic.modules.file.entity.File();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        fileToSave.setId(UUID.randomUUID().toString());
        fileToSave.setName(project.equals("CUSTOMER_REGISTER") ? fileName : UUID.randomUUID().toString());
        fileToSave.setUrl("");
        fileToSave.setSize(file.getSize());
        fileToSave.setType(file.getContentType());
        fileToSave.setProject(project);
        fileToSave.setIsInFolder(true);
        fileToSave.setIsTemp(false);

        String path = pathStr.isEmpty() ? project + '/' :
                project + '/' + pathStr + '/';

        try{
            uploadToFolder(fileToSave.getName(), file, baseFolder + "/" + path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Could not upload file " + file.getOriginalFilename() + " to " + project + " folder \n:" + e.getMessage());
        }

        fileToSave.setPath(path + fileToSave.getName());
        return fileToSave;
    }

    public static com.change.gic.modules.file.entity.File buildFileToSaveToDatabase(String project, MultipartFile file) throws IOException {
        com.change.gic.modules.file.entity.File fileToSave = new com.change.gic.modules.file.entity.File();
        fileToSave.setId(UUID.randomUUID().toString());
        fileToSave.setName(UUID.randomUUID().toString());
        fileToSave.setUrl("");
        fileToSave.setSize(file.getSize());
        fileToSave.setType(file.getContentType());
        fileToSave.setProject(project);
        fileToSave.setIsInFolder(false);
        fileToSave.setIsTemp(false);
        fileToSave.setData(compressFile(file.getBytes()));
        return fileToSave;
    }

    public static com.change.gic.modules.file.entity.File buildFileToUpdateToFolder(com.change.gic.modules.file.entity.File fileFromDatabase, String pathStr, MultipartFile file, String baseFolder)
            throws IOException {
        String filename = UUID.randomUUID().toString();
        fileFromDatabase.setType(file.getContentType());
        fileFromDatabase.setSize(file.getSize());
        fileFromDatabase.setName(filename);
        deleteFile(fileFromDatabase.getPath());
        String path = pathStr.isEmpty() ? fileFromDatabase.getProject() + '/' :
                fileFromDatabase.getProject() + '/' + pathStr + '/';
        try {
            uploadToFolder(filename, file, baseFolder + "/" + path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Could not upload file " + file.getOriginalFilename() + " to " + fileFromDatabase.getProject() + " folder \n:" + e.getMessage());
        }
        fileFromDatabase.setPath(path + filename);
        return fileFromDatabase;
    }


    public static MultipartFile bytesToMultipartFile(byte[] bytes, String name, String fileName, String contentType) {
        return new MockMultipartFile(
                name,                  // name
                fileName,          // original filename
                contentType,       // content type
                bytes                    // content
        );
    }

    public static String getBase64FromFile(String path){
        try {
            byte[] byteData = Files.readAllBytes(Paths.get(path));
            return Base64.getEncoder().encodeToString(byteData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
