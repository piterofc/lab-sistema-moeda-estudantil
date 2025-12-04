package com.universidade.moedaestudantil.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender sender;

    public EmailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void sendMail(String para, String assunto, String conteudo) throws MessagingException {

        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);

            sender.send(message);
            System.out.println("Email enviado com sucesso para " + para);
            
        } catch (MessagingException e) {
            System.err.println("Erro ao enviar email: " + e.getMessage());
            throw e;
        }
    }
}