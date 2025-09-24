package com.cybercity.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.cybercity.application.entities.UserEntity;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendTeamsMeetingInvite(String to, String name, String title, String sessionId, String date, String time, String meetingLink) throws MessagingException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("abhay28rahul03@gmail.com");
        helper.setTo(to);
        helper.setSubject(title);

        // Set Thymeleaf context
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("title", title);
        context.setVariable("sessionId", sessionId);
        context.setVariable("date", date);
        context.setVariable("time", time);
        context.setVariable("meetingLink", meetingLink);

        String html = templateEngine.process("email-template", context);
        helper.setText(html, true);

        mailSender.send(mimeMessage);
    }
    
    
    public void sendVerificationEmail(UserEntity user, String token) {
        String subject = "Email Verification - Cyberdiction Technology";
        String verificationUrl = "http://localhost:4200/verify?token=" + token;

        String body = "Hello " + user.getUserName() + ",\n\n" +
                "Please click the link below to verify your email:\n" + verificationUrl +
                "\n\nThis link will expire in 24 hours.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

}

