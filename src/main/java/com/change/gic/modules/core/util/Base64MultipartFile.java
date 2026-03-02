package com.change.gic.modules.core.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.Base64;

public class Base64MultipartFile implements MultipartFile {

    private final byte[] content;
    private final String name;
    private final String originalFilename;
    private final String contentType;

    public Base64MultipartFile(String base64Content, String filename, String contentType) {
        this.content          = Base64.getDecoder().decode(base64Content);
        this.name             = filename;
        this.originalFilename = filename;
        this.contentType      = contentType;
    }

    @Override public String getName()             { return name; }
    @Override public String getOriginalFilename() { return originalFilename; }
    @Override public String getContentType()      { return contentType; }
    @Override public boolean isEmpty()            { return content == null || content.length == 0; }
    @Override public long getSize()               { return content.length; }
    @Override public byte[] getBytes()            { return content; }
    @Override public InputStream getInputStream() { return new ByteArrayInputStream(content); }

    @Override
    public void transferTo(File dest) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(content);
        }
    }
}
