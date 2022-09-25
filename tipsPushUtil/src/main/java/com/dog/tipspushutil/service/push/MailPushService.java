package com.dog.tipspushutil.service.push;

import com.dog.tipspushutil.constant.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailPushService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${mail.receiver}")
    private String[] receiver;

    @Value("${mail.adminHandler}")
    private String adminHandler;

    private final Logger logger = LoggerFactory.getLogger(MailPushService.class);

    //群发
    public void sendMail(String subject, String content) throws InterruptedException {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        if (subject.equalsIgnoreCase(MessageType.REQUESTEXCEPTION)) {
            mailMessage.setTo(adminHandler);
        } else {
            mailMessage.setTo(receiver);
        }
        try {
            mailSender.send(mailMessage);
            logger.warn("send mail success , content = {}", mailMessage.toString());
        } catch (MailException e) {
            logger.error("send mail error:{}", e.getMessage());
            Thread.sleep(60 * 1000);
            mailSender.send(mailMessage);
        }
    }


}
