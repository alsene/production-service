package com.production.api.service;

import com.production.api.model.Produit;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class SrvPdfProduction {

    public byte[] genererPdfProduitsEncours(List<Produit> produits) {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDType1Font fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream content = new PDPageContentStream(document, page);
            float y = 770;

            content.setFont(fontBold, 14);
            content.beginText();
            content.newLineAtOffset(50, y);
            content.showText("Produits en cours");
            content.endText();

            y -= 25;
            content.setFont(fontRegular, 10);
            content.beginText();
            content.newLineAtOffset(50, y);
            content.showText("Code | Nom | Quantite | Qualite | Encours");
            content.endText();

            y -= 15;
            content.moveTo(50, y);
            content.lineTo(550, y);
            content.stroke();
            y -= 15;

            for (Produit produit : produits) {
                if (y < 60) {
                    content.close();
                    page = new PDPage();
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = 770;
                    content.setFont(fontRegular, 10);
                }

                String line = String.format(
                        "%s | %s | %s | %s | %s",
                        safe(produit.getCode()),
                        safe(produit.getNom()),
                        safeBigDecimal(produit.getQuantite()),
                        produit.getQualite() != null ? produit.getQualite().name() : "-",
                        Boolean.TRUE.equals(produit.getEncours()) ? "Oui" : "Non"
                );

                // Eviter un texte trop long qui depasse horizontalement la page.
                if (line.length() > 110) {
                    line = line.substring(0, 107) + "...";
                }

                content.beginText();
                content.newLineAtOffset(50, y);
                content.showText(line);
                content.endText();
                y -= 14;
            }

            content.close();
            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("Erreur lors de la generation du PDF des produits en cours", e);
            throw new IllegalStateException("Impossible de generer le PDF des produits en cours", e);
        }
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private String safeBigDecimal(BigDecimal value) {
        return value == null ? "-" : value.toPlainString();
    }
}

