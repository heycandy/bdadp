package com.chinasofti.ark.bdadp.service.schedule.impl;

import com.chinasofti.ark.bdadp.service.PropsService;
import com.chinasofti.ark.bdadp.util.common.StringUtils;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class MailService {

  private final Logger log = LoggerFactory.getLogger(MailService.class);
  private final String CODING = "defaultEncoding";
  private final String MAIL_AUTH = "mail.smtp.auth";
  private final String TTLS = "mail.smtp.starttls.enable";

  private final String MAIL_HOST = "mail.hosturl";
  private final String MAIL_PORT = "mail.port";
  private final String MAIL_USER = "mail.sender.username";
  private final String MAIL_PWD = "mail.sender.pwd";


  @Async
  public void sendEmail(String subject, String content, String[] receivers, String copyTo) {
    if (StringUtils.isBlank(receivers)) {
      log.info("mail receivers is null !!!!");
      return;
    }

    JavaMailSenderImpl sender = getSender();
    MimeMessage mineMSG = sender.createMimeMessage();
    String recs = StringUtils.join(receivers, ",");
    try {
      MimeMessageHelper msg = new MimeMessageHelper(mineMSG, false, CharEncoding.UTF_8);
      msg.setTo(receivers);
      msg.setFrom(sender.getUsername());
      msg.setSubject(subject);
      msg.setText(content, false);
      if (StringUtils.isNotEmpty(copyTo)) {
        msg.setCc(copyTo);
      }
      sender.send(mineMSG);
      log.info("Email sent success to users '{}'", recs);
    } catch (MessagingException e) {
      log.warn("Email sent faild to users '{}', exception is: {}", recs, e.getMessage());
    }
  }


  @Async
  public JavaMailSenderImpl getSender() {

    JavaMailSenderImpl sender = new JavaMailSenderImpl();
    Properties props = PropsService.getConfigProps();

    sender.setHost(props.getProperty(MAIL_HOST));
    sender.setPort(Integer.parseInt(props.getProperty(MAIL_PORT)));
    sender.setUsername(props.getProperty(MAIL_USER));
    sender.setPassword(props.getProperty(MAIL_PWD));

    Properties sendProps = new Properties();
    sendProps.setProperty(CODING, "utf-8");
    sendProps.setProperty(MAIL_AUTH, "false");
    sendProps.setProperty(TTLS, "false");
    sender.setJavaMailProperties(sendProps);
    return sender;
  }

}
