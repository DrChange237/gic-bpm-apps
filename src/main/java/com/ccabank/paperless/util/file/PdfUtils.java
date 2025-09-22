package com.ccabank.paperless.util.file;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.*;
import java.util.List;

public class PdfUtils {

    public static byte[] addFooterToPdf(byte[] inputPdf, String footerText) throws Exception {
        PdfReader reader = new PdfReader(new ByteArrayInputStream(inputPdf));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, outputStream);

        int numberOfPages = reader.getNumberOfPages();
        for (int i = 1; i <= numberOfPages; i++) {
            PdfContentByte canvas = stamper.getOverContent(i);

            // Définir la police
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            canvas.beginText();
            canvas.setFontAndSize(bf, 10);

            // Récupérer la taille de la page
            Rectangle pageSize = reader.getPageSize(i);

            // Position en bas à droite (50 unités de marge depuis le bord droit, 20 depuis le bas)
            float x = pageSize.getRight() - 50;
            float y = pageSize.getBottom() + 20;

            canvas.showTextAligned(Element.ALIGN_RIGHT, footerText, x, y, 0);
            canvas.endText();
        }

        stamper.close();
        reader.close();

        return outputStream.toByteArray();
    }

    public static byte[] addWatermark(byte[] pdfBytes, String watermarkText) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(pdfBytes));
        PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);

        int numberOfPages = pdfReader.getNumberOfPages();
        Font font = new Font(Font.FontFamily.HELVETICA, 50, Font.BOLD, new BaseColor(200, 200, 200, 50)); // gris clair

        Phrase phrase = new Phrase(watermarkText, font);

        for (int i = 1; i <= numberOfPages; i++) {
            PdfContentByte content = pdfStamper.getOverContent(i); // au-dessus du contenu
            ColumnText.showTextAligned(
                    content,
                    Element.ALIGN_CENTER,
                    phrase,
                    pdfReader.getPageSizeWithRotation(i).getWidth() / 2,
                    pdfReader.getPageSizeWithRotation(i).getHeight() / 2,
                    45 // rotation angle
            );
        }

        pdfStamper.close();
        pdfReader.close();

        return outputStream.toByteArray();
    }

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
