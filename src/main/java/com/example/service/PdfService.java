package com.example.service;

import com.example.model.User;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import com.lowagie.text.FontFactory;
import java.awt.Color;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {
    public byte[] generateTicketPdf(User user, byte[] qrCodeBytes) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // Add the Fearless logo at the top
        try {
            Image logo = Image.getInstance(getClass().getClassLoader().getResource("static/logoz.png"));
            logo.scaleToFit(120, 120);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            // If logo fails to load, continue without it
        }

        // Add a colored border (Fearless orange)
        Rectangle page = document.getPageSize();
        PdfContentByte canvas = writer.getDirectContent();
        canvas.setColorStroke(new Color(255, 102, 0)); // #FF6600
        canvas.setLineWidth(6f);
        canvas.rectangle(10, 10, page.getWidth() - 20, page.getHeight() - 20);
        canvas.stroke();

        // Event Title (Fearless orange)
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, new Color(255, 102, 0));
        Paragraph title = new Paragraph("FEARLESS CAMP TICKET", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Subtitle (darker orange)
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(204, 85, 0));
        Paragraph subtitle = new Paragraph("Welcome to The Fearless Movement!", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);
        document.add(new Paragraph(" "));

        // User Info Table (light orange background for labels)
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, new Color(255, 102, 0));
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 13, new Color(50, 50, 50));
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(80);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setSpacingBefore(16f);
        table.setSpacingAfter(16f);
        String[][] info = {
            {"Name:", user.getFirstName() + " " + user.getLastName()},
            {"Email:", user.getEmail()},
            {"Phone:", user.getPhone()},
            {"Reference:", user.getPaymentReference()}
        };
        for (String[] row : info) {
            PdfPCell cell1 = new PdfPCell(new Phrase(row[0], labelFont));
            cell1.setBorder(0);
            cell1.setBackgroundColor(new Color(255, 236, 220)); // very light orange
            cell1.setPadding(8f);
            table.addCell(cell1);
            PdfPCell cell2 = new PdfPCell(new Phrase(row[1], valueFont));
            cell2.setBorder(0);
            cell2.setBackgroundColor(Color.WHITE);
            cell2.setPadding(8f);
            table.addCell(cell2);
        }
        document.add(table);

        // QR Code with label (orange accent)
        Font qrLabelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(255, 102, 0));
        Paragraph qrLabel = new Paragraph("Scan to Check In", qrLabelFont);
        qrLabel.setAlignment(Element.ALIGN_CENTER);
        document.add(qrLabel);
        document.add(new Paragraph(" "));
        Image qrImage = Image.getInstance(qrCodeBytes);
        qrImage.scaleToFit(160, 160);
        qrImage.setAlignment(Element.ALIGN_CENTER);
        qrImage.setBorder(Image.BOX);
        qrImage.setBorderWidth(3f);
        qrImage.setBorderColor(new Color(255, 102, 0));
        document.add(qrImage);
        document.add(new Paragraph(" "));

        // Instructions (fun, inviting)
        Font instrFont = FontFactory.getFont(FontFactory.HELVETICA, 12, new Color(80, 80, 80));
        Paragraph instr = new Paragraph(
            "Bring this ticket (on your phone or printed). Get ready for games, worship, new friends, and unforgettable moments!\n" +
            "Scan the QR code at the entrance to check in and start your journey!",
            instrFont
        );
        instr.setAlignment(Element.ALIGN_CENTER);
        document.add(instr);
        document.add(new Paragraph(" "));

        // Inspirational quote (footer)
        Font quoteFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 11, new Color(120, 120, 120));
        Paragraph quote = new Paragraph(
            "\"Arise, shine, for your light has come, and the glory of the LORD rises upon you.\" - Isaiah 60:1",
            quoteFont
        );
        quote.setAlignment(Element.ALIGN_CENTER);
        document.add(quote);

        document.close();
        return baos.toByteArray();
    }
} 