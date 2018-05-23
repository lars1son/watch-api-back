package com.edsson.expopromoter.api.operator;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("javasampleapproachMailSender")
public class MailSender {

    @Autowired
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    private static String subject = "Password reset";

    Logger logger = Logger.getLogger(this.getClass());

    public void sendMail(String to, String body) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(sender);
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);

        logger.info("Sending...");

        javaMailSender.send(mail);

        logger.info("Done!");
    }
}