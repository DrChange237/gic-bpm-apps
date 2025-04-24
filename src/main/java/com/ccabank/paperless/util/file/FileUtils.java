package com.ccabank.paperless.util.file;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Base64;

public class FileUtils {


    public static  ByteArrayResource mergeResources(ByteArrayResource resource1, ByteArrayResource resource2) throws IOException {
        byte[] bytes1 = resource1.getByteArray();
        byte[] bytes2 = resource2.getByteArray();

        // Crée un tableau de bytes pour contenir les deux ressources
        byte[] mergedBytes = new byte[bytes1.length + bytes2.length];

        // Copie les données du premier tableau
        System.arraycopy(bytes1, 0, mergedBytes, 0, bytes1.length);
        // Copie les données du second tableau
        System.arraycopy(bytes2, 0, mergedBytes, bytes1.length, bytes2.length);

        return new ByteArrayResource(mergedBytes);
    }

    public static MultipartFile convertToMultipartFile(ByteArrayResource byteArrayResource, String fileName) {

        return new MultipartFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return "application/pdf"; // ou un type MIME approprié
            }

            @Override
            public boolean isEmpty() {
                return byteArrayResource.contentLength() == 0;
            }

            @Override
            public long getSize() {
                return byteArrayResource.contentLength();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return byteArrayResource.getByteArray();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(byteArrayResource.getByteArray());
            }

            @Override
            public Resource getResource() {
                return MultipartFile.super.getResource();
            }

            @Override
            public void transferTo(File file) throws IOException, IllegalStateException {
            }

            @Override
            public void transferTo(Path dest) throws IOException, IllegalStateException {
                MultipartFile.super.transferTo(dest);
            }
        };
    }

    public static MultipartFile convertBase64ToMultipartFile(String base64Content, String fileName, String contentType) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);
        return new MultipartFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public boolean isEmpty() {
                return decodedBytes.length == 0;
            }

            @Override
            public long getSize() {
                return decodedBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return decodedBytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return inputStream;
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                // Implémentation de la méthode transferTo()
            }
        };
    }
}
