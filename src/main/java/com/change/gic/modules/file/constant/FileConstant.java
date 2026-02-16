package com.change.gic.modules.file.constant;

import java.util.Arrays;
import java.util.List;

public class FileConstant {
    public static final long MAX_FILE_SIZE = 1024 * 1024 * 5;
    public static final long MAX_IMAGE_FILE_SIZE = 1024 * 1024 * 10;
    public static final long MAX_DOCUMENT_FILE_SIZE = 1024 * 1024 * 10;
    public static final long MAX_VIDEO_FILE_SIZE = 1024 * 1024 * 10;


    public static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "jpg",//JPEG File Interchange Format
            "jpeg",//JPEG File Interchange Format
            "png",//PNG Portable Network Graphics Format
            "gif",
            "docx",//Word Document
            "doc",//Word 97-2003 Document
            "docm",//Word Macro-Enabled Document
            "odt",//OpenDocument Text
            "csv",//CSV
            "ods",
            "xlsx",
            "pdf",
            "txt",//Plain Text
            "xml",//Word XML Document
            "xps",//XPS Document
            "ppt",
            "pptx",
            "xls",//Excel 97-Excel 2003 Workbook
            "xlsm",//Excel Macro-Enabled Workbook
            "xlsb",//Excel Binary Workbook
            "xlsx",//Excel WorkBook
            "gif",//GIF Graphics Interchange Format
            "mp4" //MPEG-4 Video
    );
}
