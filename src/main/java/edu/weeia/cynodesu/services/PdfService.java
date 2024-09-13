package edu.weeia.cynodesu.services;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import edu.weeia.cynodesu.api.v1.model.CompetitionScoreForm;
import edu.weeia.cynodesu.configuration.CertConfig;
import edu.weeia.cynodesu.domain.Competition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class PdfService {

    private final CertConfig certConfig;
    private final String title;
    private final String subtitle;
    private final String footer;
    private final String registrar;
    private final String backgroundImagePath;
    private final String organizationIconPath;

    public PdfService(CertConfig certConfig) {
        this.certConfig = certConfig;
        this.title = certConfig.getTitle();
        this.subtitle = certConfig.getSubtitle();
        this.footer = certConfig.getFooter();
        this.registrar = certConfig.getRegistrar();
        this.backgroundImagePath = certConfig.getBackgroundImagePath();
        this.organizationIconPath = certConfig.getOrganizationIconPath();
    }

    public byte[] generateDogCertificate(CompetitionScoreForm scoreForm, Competition competition, String registrationNo) throws Exception {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            // Initialize PDF writer and document
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Set Background Image
            InputStream backgroundImageStream = getClass().getClassLoader().getResourceAsStream(backgroundImagePath);
            InputStream iconImageStream = getClass().getClassLoader().getResourceAsStream(organizationIconPath);
            assert backgroundImageStream != null;
            assert iconImageStream != null;
            Image background = new Image(ImageDataFactory.create(backgroundImageStream.readAllBytes()));
            background.setFixedPosition(0, 0);
            background.setOpacity(0.3f); // Adjust the opacity
            document.add(background);

            // Add Title
            Paragraph titleParagraph = new Paragraph(title)
                    .setFont(PdfFontFactory.createFont())
                    .setFontSize(24)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold();
            document.add(titleParagraph);

            // Add Subtitle
            Paragraph subtitleParagraph = new Paragraph(subtitle)
                    .setFont(PdfFontFactory.createFont())
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(subtitleParagraph);

            // Add Handler's and Dog's Name
            Paragraph handlerParagraph = new Paragraph(scoreForm.getBreedingFacilityName())
                    .setFont(PdfFontFactory.createFont())
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold();
            document.add(handlerParagraph);

            Paragraph dogNameParagraph = new Paragraph(scoreForm.getDogName())
                    .setFont(PdfFontFactory.createFont())
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold();
            document.add(dogNameParagraph);

            // Add Registration Information
            Paragraph registrationInfo = new Paragraph("Certified Service Dog\nU S Service Dogs Registry Number")
                    .setFont(PdfFontFactory.createFont())
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(registrationInfo);

            Paragraph registrationNumberParagraph = new Paragraph(registrationNo)
                    .setFont(PdfFontFactory.createFont())
                    .setFontSize(24)
                    .setFontColor(new DeviceRgb(255, 0, 0)) // Red color
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(registrationNumberParagraph);

            // Add Organization Icon
            Image icon = new Image(ImageDataFactory.create(iconImageStream.readAllBytes()));
            icon.setFixedPosition(400, 700); // Adjust position
            document.add(icon);

            // Add Issued Date and Registrar Information
            Paragraph issuedDateParagraph = new Paragraph("Issued " + LocalDate.now())
                    .setFont(PdfFontFactory.createFont())
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(issuedDateParagraph);

            Paragraph registrarParagraph = new Paragraph(registrar)
                    .setFont(PdfFontFactory.createFont())
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(registrarParagraph);

            // Add Footer
            Paragraph footerParagraph = new Paragraph(footer)
                    .setFont(PdfFontFactory.createFont())
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(footerParagraph);

            // Close the document
            document.close();

            return byteArrayOutputStream.toByteArray();
        }
    }
}
