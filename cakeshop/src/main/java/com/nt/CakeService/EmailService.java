package com.nt.CakeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBillEmail(String toEmail, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setFrom("chaudharichinmay227@gmail.com"); // ✅ Always set your Gmail address here
            helper.setSubject("Your Cake Shop Bill");
            helper.setText("""
                    Dear Customer,
                    
                    Please find your bill attached.
                    
                    Thank you for shopping with us!
                    Cake Shop
                    """);

            // ✅ Cleaner attachment method using ByteArrayResource
            helper.addAttachment("bill.pdf", new ByteArrayResource(pdfBytes));

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to send email: " + e.getMessage(), e);
        }
    }
}
