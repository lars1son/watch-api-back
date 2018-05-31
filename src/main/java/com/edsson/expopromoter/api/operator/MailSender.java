package com.edsson.expopromoter.api.operator;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Component("javasampleapproachMailSender")
public class MailSender {
    private final static Logger logger = Logger.getLogger(MailSender.class);


    @Value("${spring.mail.username}")
    private String sender;

    @Value("${expopromoter.system.config.password.reset_ling_base_url}")
    private String websiteUrlPassword;

    @Value("${expopromoter.system.config.password.reset_ling_base_email}")
    private String websiteUrlEmail;

    private static final long MAIL_SEND_DELAY = 3000;


    private Queue<MimeMessagePreparator> queue = new LinkedBlockingQueue<>();


    private static String subjectPasswordReset = "Password reset";
    private static String subjectEmailReset = "Email reset";

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void operateMessage(String email, String token, boolean pas) {
        String url;
        String subject;
        if (pas) {
            url = websiteUrlPassword + token;
            subject = subjectPasswordReset;
        } else {
            url = websiteUrlEmail + token;
            subject = subjectEmailReset;
        }

        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(email);
            message.setSubject(subject);
            message.setText("Click here to reset your password: <a href=\"" + url + "\">" + url + "</a>", true);
            message.setFrom(sender);

        };
        send(preparator);
    }

    public void send(MimeMessagePreparator preparator) {
        try {
            this.javaMailSender.send(preparator);
            logger.info(String.format("Mail successfully sent. Mail to send in queue: %d", queue.size()));
        } catch (Exception ex) {
            logger.warn("Exception sending mail");
        }
    }



    private void addToQueue(MimeMessagePreparator preparator) {
        queue.offer(preparator);
    }

}