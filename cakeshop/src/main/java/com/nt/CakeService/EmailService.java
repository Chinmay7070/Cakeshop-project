package com.nt.CakeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBillEmail(String toEmail, byte[] pdfBytes) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setFrom("chaudharichinmay227@gmal.com"); // ✅ set sender
        helper.setSubject("Your Cake Bill");
        helper.setText("Thank you for your order! Please find your bill attached.");

        // Attach PDF using ByteArrayResource
        helper.addAttachment("bill.pdf", new ByteArrayResource(pdfBytes));

        mailSender.send(message);
    }
}
