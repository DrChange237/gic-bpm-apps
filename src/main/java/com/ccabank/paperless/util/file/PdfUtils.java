package com.ccabank.paperless.util.file;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.util.List;

public class PdfUtils {

    public static byte[] mergePdfs(List<byte[]> pdfFiles) {
        Document document = new Document(PageSize.LETTER);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfCopy copy = new PdfCopy(document, outputStream);
            document.open();

            for (byte[] pdfBytes : pdfFiles) {
                PdfReader reader = new PdfReader(new ByteArrayInputStream(pdfBytes));
                copy.addDocument(reader);
                reader.close();
            }

            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            try {
                outputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        return outputStream.toByteArray();
    }

}
