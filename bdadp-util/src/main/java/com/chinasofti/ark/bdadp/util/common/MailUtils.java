package com.chinasofti.ark.bdadp.util.common;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;


/**
 * Created by water on 2017.7.24.
 */
public class MailUtils {

  private static Logger log = LoggerFactory.getLogger(MailUtils.class);
  public static String MAIL_HOST = "smtp.163.com";
  public static int MAIL_PORT = 25;
  public static String MAIL_USER = "ark_schedule@163.com";
  public static String MAIL_PWD = "huateng123";


  public static void sendMails(String subject, String content, String[] receivers) {
    sendMails(subject, content, receivers, getSender());
  }

  public static void sendMails(String subject, String content, String[] receivers,
      JavaMailSenderImpl sender) {
    MimeMessage mimeMessage = sender.createMimeMessage();
    MimeMessageHelper message;
    try {
      message = new MimeMessageHelper(mimeMessage, false, CharEncoding.UTF_8);
      message.setTo(receivers);
      message.setFrom(sender.getUsername());
      message.setSubject(subject);
      message.setText(content);
      sender.send(mimeMessage);
      log.info(" =========== from " + MAIL_USER + " to " + org.apache.commons.lang.StringUtils
          .join(receivers, ",") + " send email success !!!");

    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  public static JavaMailSenderImpl getSender() {

    JavaMailSenderImpl sender = new JavaMailSenderImpl();
    sender.setHost(MAIL_HOST);
    sender.setPort(MAIL_PORT);
    sender.setUsername(MAIL_USER);
    sender.setPassword(MAIL_PWD);

    Properties sendProps = new Properties();
    sendProps.setProperty("defaultEncoding", "utf-8");
    sendProps.setProperty("mail.smtp.auth", "false");
    sendProps.setProperty("mail.smtp.starttls.enable", "false");
    sender.setJavaMailProperties(sendProps);
    return sender;
  }

}
