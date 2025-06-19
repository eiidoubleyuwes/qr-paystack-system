package com.example.service;

import com.example.model.User;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {
    public byte[] generateTicketPdf(User user, byte[] qrCodeBytes) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        document.add(new Paragraph("Ticket for: " + user.getFirstName() + " " + user.getLastName()));
        document.add(new Paragraph("Email: " + user.getEmail()));
        document.add(new Paragraph("Phone: " + user.getPhone()));
        document.add(new Paragraph("Reference: " + user.getPaymentReference()));
        document.add(new Paragraph(""));
        Image qrImage = Image.getInstance(qrCodeBytes);
        qrImage.scaleToFit(200, 200);
        document.add(qrImage);
        document.close();
        return baos.toByteArray();
    }
} 