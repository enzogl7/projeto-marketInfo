package com.ogl.MarketInfo.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    public String enviarEmailNotificacao(String destinatario, String assunto, String mensagem) {
        try {
            Context context = new Context();
            context.setVariable("mensagem", mensagem);

            String htmlContent = templateEngine.process("emails/notificacao_estoque", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            return "Email enviado com sucesso!";
        } catch (Exception e) {
            return "Erro ao enviar email. " + e.getMessage();
        }
    }

}
