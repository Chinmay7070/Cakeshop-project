package com.nt.CakeService;

import org.springframework.stereotype.Service;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.nt.model.Cake;

@Service
public class PdfService {
     
	public byte[] generateBillPdf(Cake cake,String customerEmail) {
		ByteArrayOutputStream baos  = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document  = new Document(pdf);
		
		document.add(new Paragraph("Cake Bill"));
        document.add(new Paragraph("Cake: " + cake.getName()));
        document.add(new Paragraph("Price: ₹" + cake.getPrice()));
        document.add(new Paragraph("Email: " + customerEmail));
        document.close();

        return baos.toByteArray();
	}
}
