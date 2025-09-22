package com.ccabank.paperless.util.file;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfUtils {

    /**
     * Récupère un paramètre personnalisé depuis un PDF.
     *
     * @param pdfBytes  PDF sous forme de byte[]
     * @param paramName nom du paramètre
     * @return valeur du paramètre ou null si non trouvé
     * @throws Exception
     */
    public static String getParameter(byte[] pdfBytes, String paramName) throws Exception {
        PdfReader reader = new PdfReader(new ByteArrayInputStream(pdfBytes));
        Map<String, String> info = reader.getInfo();
        reader.close();
        return info.get(paramName);
    }

    /**
     * Ajoute un paramètre personnalisé à un PDF existant.
     *
     * @param pdfBytes   PDF d'entrée sous forme de byte[]
     * @param paramName  nom du paramètre
     * @param paramValue valeur du paramètre
     * @return PDF modifié sous forme de byte[]
     * @throws Exception
     */
    public static byte[] addParameter(byte[] pdfBytes, String paramName, String paramValue) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(pdfBytes);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfReader reader = new PdfReader(bais);
        PdfStamper stamper = new PdfStamper(reader, baos);

        // Récupérer les métadonnées existantes
        Map<String, String> info = new HashMap<>(reader.getInfo());
        // Ajouter ou remplacer le paramètre
        info.put(paramName, paramValue);

        stamper.setMoreInfo(info);
        stamper.close();
        reader.close();

        return baos.toByteArray();
    }

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
            canvas.setFontAndSize(bf, 8);

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

    public static byte[] removeLastPage(byte[] pdfBytes) {
        try {
            PdfReader reader = new PdfReader(new ByteArrayInputStream(pdfBytes));
            int totalPages = reader.getNumberOfPages();

            if (totalPages <= 1) {
                throw new IllegalArgumentException("Le document ne contient pas assez de pages pour supprimer la dernière.");
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, baos);

            document.open();

            // Copier toutes les pages sauf la dernière
            for (int i = 1; i < totalPages; i++) {
                copy.addPage(copy.getImportedPage(reader, i));
            }

            document.close();
            reader.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la dernière page du PDF", e);
        }
    }

}
