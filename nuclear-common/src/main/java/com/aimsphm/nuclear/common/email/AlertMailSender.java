package com.aimsphm.nuclear.common.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
@ConditionalOnProperty(prefix = "spring.mailConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class AlertMailSender {
    @Autowired
    private JavaMailSender sender;
    private static final Logger LOGGER = LoggerFactory.getLogger(AlertMailSender.class);

    public void sendMail(Mail mail) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(new InternetAddress(mail.getFrom()));
            helper.setTo(mail.getToList());
            if (mail.getCcList() != null && mail.getCcList().length != 0) {
                helper.setCc(mail.getCcList());
            } else {
                helper.setCc(new String[0]);
            }
            helper.setSubject(mail.getSubject());
            helper.setText("<html>" + mail.getHeader() + "</br></br>" + mail.getBody() + "</br></br>" + mail.getFooter() + "</html>", true);
        } catch (MessagingException e) {
            LOGGER.error("sending email error", e);
        }
        sender.send(message);
    }
}
