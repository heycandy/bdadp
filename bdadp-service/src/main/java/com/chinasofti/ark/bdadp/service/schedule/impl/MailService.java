package com.chinasofti.ark.bdadp.service.schedule.impl;

import com.chinasofti.ark.bdadp.service.PropsService;
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
//  private Properties currentProps = PropsService.getConfigProps();
//  private String host = currentProps.getProperty("mail.hosturl");
//  private Integer port = Integer.parseInt(currentProps.getProperty("mail.port"));
//  private String user = currentProps.getProperty("mail.sender.username");
//  private String pwd = currentProps.getProperty("mail.sender.pwd");

  @Async
  public void sendEmail(String subject, String content, String[] receivers) {

    JavaMailSenderImpl sender = getSender();
    MimeMessage mimeMessage = sender.createMimeMessage();
    try {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, CharEncoding.UTF_8);
      message.setTo(receivers);
      message.setFrom(sender.getUsername());
      message.setSubject(subject);
      message.setText(content);
      sender.send(mimeMessage);
      log.info(
          " =========== from " + sender.getUsername() + " to " + org.apache.commons.lang.StringUtils
              .join(receivers, ",") + " send email success !!!");

    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }


  @Async
  public JavaMailSenderImpl getSender() {

    JavaMailSenderImpl sender = new JavaMailSenderImpl();

    Properties currentProps = PropsService.getConfigProps();
    String host = currentProps.getProperty("mail.hosturl");
    Integer port = Integer.parseInt(currentProps.getProperty("mail.port"));
    String user = currentProps.getProperty("mail.sender.username");
    String pwd = currentProps.getProperty("mail.sender.pwd");

    sender.setHost(host);
    sender.setPort(port);
    sender.setUsername(user);
    sender.setPassword(pwd);

    Properties sendProps = new Properties();
    sendProps.setProperty("defaultEncoding", "utf-8");
    sendProps.setProperty("mail.smtp.auth", "false");
    sendProps.setProperty("mail.smtp.starttls.enable", "false");
    sender.setJavaMailProperties(sendProps);
    return sender;
  }
}
