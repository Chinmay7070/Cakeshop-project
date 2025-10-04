
package com.nt.CakeService;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.nt.model.Cake;

@Service
public class PdfService {

    public byte[] generateBillPdf(Cake cake, String customerEmail) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Cake Shop - Bill"));
        document.add(new Paragraph("-------------------------"));
        document.add(new Paragraph("Customer Email: " + customerEmail));
        document.add(new Paragraph("-------------------------"));
        document.add(new Paragraph("Cake: " + cake.getName()));
        document.add(new Paragraph("Price: ₹" + cake.getPrice()));
        document.add(new Paragraph("Quantity: 1"));
        document.add(new Paragraph("Total Price: ₹" + cake.getPrice()));
        document.add(new Paragraph("Thank you for shopping with us!"));
        document.close();

        return baos.toByteArray();
    }

    public byte[] generateCartBillPdf(List cartDetails, String customerName, String customerEmail, String customerPhone, String customerAddress) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Cake Shop - Bill"));
        document.add(new Paragraph("-------------------------"));
        document.add(new Paragraph("Customer Details:"));
        document.add(new Paragraph("Name: " + (customerName != null ? customerName : "Unknown")));
        document.add(new Paragraph("Email: " + (customerEmail != null ? customerEmail : "N/A")));
        document.add(new Paragraph("Phone: " + (customerPhone != null ? customerPhone : "N/A")));
        document.add(new Paragraph("Address: " + (customerAddress != null ? customerAddress : "N/A")));
        document.add(new Paragraph("-------------------------"));
        document.add(new Paragraph("Order Details:"));
        double totalPrice = 0;
        for (int i = 0; i < cartDetails.size(); i++) {
            CartDetail cartDetail = (CartDetail) cartDetails.get(i);
            double itemPrice = cartDetail.getCakePrice() * cartDetail.getQuantity();
            totalPrice = totalPrice + itemPrice;
            document.add(new Paragraph("Cake: " + cartDetail.getCakeName()));
            document.add(new Paragraph("Quantity: " + cartDetail.getQuantity()));
            document.add(new Paragraph("Price per unit: ₹" + cartDetail.getCakePrice()));
            document.add(new Paragraph("Item Total: ₹" + itemPrice));
            document.add(new Paragraph("-------------------------"));
        }
        document.add(new Paragraph("Total Price: ₹" + totalPrice));
        document.add(new Paragraph("Thank you for shopping with us!"));
        document.close();

        return baos.toByteArray();
    }
}
