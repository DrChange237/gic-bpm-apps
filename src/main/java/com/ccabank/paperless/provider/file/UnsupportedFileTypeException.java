package com.ccabank.paperless.provider.file;


public class UnsupportedFileTypeException extends Exception {

    private static final long serialVersionUID = 1L;

    public UnsupportedFileTypeException(String message) {
        super(message);
    }
}
