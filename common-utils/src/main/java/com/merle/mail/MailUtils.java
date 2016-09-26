//package com.merle.mail;
//
//import com.lenxeon.plat.base.ExcutorUtil;
//import com.lenxeon.utils.basic.Config;
//import com.sun.mail.util.MailSSLSocketFactory;
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//
//import javax.activation.DataHandler;
//import javax.activation.FileDataSource;
//import javax.mail.*;
//import javax.mail.internet.*;
//import java.io.File;
//import java.io.IOException;
//import java.net.Authenticator;
//import java.net.PasswordAuthentication;
//import java.security.GeneralSecurityException;
//import java.util.Date;
//import java.util.Properties;
//
//public class MailUtils {
//
//    private static MailUtils instance;
//
//    private static String host = "smtp.163.com";
//
//    private static String acc = "lenxeon@163.com";
//
//    private static String accName = "lenxeon";
//
//    private static String password = "Sample09";
//
//    private PopupAuthenticator au;
//
//    private static Logger logger = Logger.getLogger(MailUtils.class);
//
//
//    private class PopupAuthenticator extends Authenticator {
//        String username = null;
//        String password = null;
//
//        public PopupAuthenticator(String user, String pass) {
//            this.username = user;
//            this.password = pass;
//        }
//
//        protected PasswordAuthentication getPasswordAuthentication() {
//
//            return new PasswordAuthentication(this.username, this.password);
//        }
//    }
//
//    static class SingletonHolder {
//        static MailUtils instance = new MailUtils();
//    }
//
//    public static MailUtils getInstance() {
//        String host = Config.getLocalProperty("mail.host");
//        if (StringUtils.isNotBlank(host)) {
//            MailUtils.host = host;
//        }
//        String acc = Config.getLocalProperty("mail.acc");
//        if (StringUtils.isNotBlank(acc)) {
//            MailUtils.acc = acc;
//        }
//        String accName = Config.getLocalProperty("mail.accName");
//        if (StringUtils.isNotBlank(accName)) {
//            MailUtils.accName = accName;
//        }
//        String password = Config.getLocalProperty("mail.password");
//        if (StringUtils.isNotBlank(password)) {
//            MailUtils.password = password;
//        }
//        return SingletonHolder.instance;
//    }
//
//    public void sendMail(String[] to, String[] copyTo, String subject, String body, File[] files) {
//        this.au = new PopupAuthenticator(accName, password);
//        Properties props = System.getProperties();
//        props.put("mail.smtp.host", host);
//        props.put("mail.smtp.auth", "true");
//        Session session = Session.getDefaultInstance(props, this.au);
//        session.setDebug(true);
//        try {
//            MimeMessage msg = new MimeMessage(session);
//            msg.setFrom(new InternetAddress(acc));
//            Address[] tos = null;
//            if (to != null) {
//                tos = new InternetAddress[to.length];
//                for (int i = 0; i < to.length; i++) {
//                    tos[i] = new InternetAddress(to[i]);
//                }
//            }
//            Address[] cos = null;
//            if (copyTo != null) {
//                cos = new InternetAddress[copyTo.length];
//                for (int i = 0; i < copyTo.length; i++) {
//                    cos[i] = new InternetAddress(copyTo[i]);
//                }
//
//            }
//            msg.setRecipients(Message.RecipientType.TO, tos);
//            msg.setRecipients(Message.RecipientType.CC, cos);
//            msg.setHeader("X-Mailer", "LOTONtechEmail");
//            msg.setSubject(subject);
//
//            Multipart mp = new MimeMultipart();
//            BodyPart mdp = new MimeBodyPart();
//            mdp.setContent(body, "text/html;charset=UTF-8");
//            mp.addBodyPart(mdp);
//            if (files != null && files.length > 0) {
//                for (File file : files) {
//                    MimeBodyPart mbp = new MimeBodyPart();
//                    FileDataSource fds = new FileDataSource(file);
//                    mbp.setDataHandler(new DataHandler(fds));
//                    mbp.setFileName(MimeUtility.encodeText(file.getName()));
//                    mp.addBodyPart(mbp);
//                }
//            }
//            msg.setContent(mp);
//            msg.setSentDate(new Date());
//            Transport.send(msg);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            e.printStackTrace();
//        } finally {
//        }
//    }
//
//    public void sendMail(String to, String copyTo, String subject, String body) {
//        sendMail(new String[]{to}, new String[]{copyTo}, subject, body, null);
//    }
//
//    public void asyncSendMail(final String to, final String copyTo, final String subject, final String body) {
//        ExcutorUtil excutorUtil = ExcutorUtil.getSingleInstance();
//        excutorUtil.execute(new Runnable() {
//            @Override
//            public void run() {
//                sendMail(to, copyTo, subject, body);
//            }
//        });
//    }
//
//    public void sendMail(String to, String subject, String body) {
//        sendMail(new String[]{to}, null, subject, body, null);
//    }
//
//    public void asyncSendMail(final String to, final String subject, final String body) {
//        ExcutorUtil excutorUtil = ExcutorUtil.getSingleInstance();
//        excutorUtil.execute(new Runnable() {
//            @Override
//            public void run() {
//                sendMail(to, subject, body);
//            }
//        });
//    }
//
//    public static void main(String[] args) throws MessagingException, IOException {
//        MailUtils mailUtils = new MailUtils();
////        mailUtils.sendMail("daidh@readingjoy.com", "daidh@readingjoy.com", "hi", "hello");
////        mailUtils.sendMail("merle_miao@163.com", "hi", "hellox");
//
//        mailUtils.SendMail("smtp.qq.com", "453673382@qq.com", "453673382@qq.com", "tjenhfnlyzjzbggi", new String[]{"43559311@qq.com"}, null, "主题", "内容", null);
//        mailUtils.SendMail("smtp.163.com", "merle_miao@163.com", "merle_miao@163.com", "a37868153", new String[]{"43559311@qq.com"}, null, "主题", "内容", null);
//
//    }
//
//    public void SendMail(String host, String acc, String accName, String password, String[] to, String[] copyTo, String subject, String body, File[] files){
//        this.au = new PopupAuthenticator(accName, password);
//        Properties props = System.getProperties();
//        props.setProperty("mail.smtp.host", host);
//        props.setProperty("mail.smtp.auth", "true");
//        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.setProperty("mail.smtp.port", "465");
//        props.setProperty("mail.smtp.socketFactory.port", "465");
//        props.setProperty("mail.smtp.socketFactory.fallback", "false");
//        MailSSLSocketFactory sf = null;
//        try {
//            sf = new MailSSLSocketFactory();
//        } catch (GeneralSecurityException e) {
//            logger.fatal("error", e);
//        }
//        sf.setTrustAllHosts(true);
//        props.put("mail.smtp.ssl.enable", "true");
//        props.put("mail.smtp.ssl.socketFactory", sf);
//        Session session = Session.getInstance(props, this.au);
//        session.setDebug(true);
//        try {
//            MimeMessage msg = new MimeMessage(session);
//            msg.setFrom(new InternetAddress(acc));
//            Address[] tos = null;
//            if (to != null) {
//                tos = new InternetAddress[to.length];
//                for (int i = 0; i < to.length; i++) {
//                    tos[i] = new InternetAddress(to[i]);
//                }
//            }
//            Address[] cos = null;
//            if (copyTo != null) {
//                cos = new InternetAddress[copyTo.length];
//                for (int i = 0; i < copyTo.length; i++) {
//                    cos[i] = new InternetAddress(copyTo[i]);
//                }
//
//            }
//            msg.setRecipients(Message.RecipientType.TO, tos);
//            msg.setRecipients(Message.RecipientType.CC, cos);
//            msg.setHeader("X-Mailer", "LOTONtechEmail");
//            msg.setSubject(subject);
//
//            Multipart mp = new MimeMultipart();
//            BodyPart mdp = new MimeBodyPart();
//            mdp.setContent(body, "text/html;charset=UTF-8");
//            mp.addBodyPart(mdp);
//            if (files != null && files.length > 0) {
//                for (File file : files) {
//                    MimeBodyPart mbp = new MimeBodyPart();
//                    FileDataSource fds = new FileDataSource(file);
//                    mbp.setDataHandler(new DataHandler(fds));
//                    mbp.setFileName(MimeUtility.encodeText(file.getName()));
//                    mp.addBodyPart(mbp);
//                }
//            }
//            msg.setContent(mp);
//            msg.setSentDate(new Date());
//            Transport.send(msg);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            e.printStackTrace();
//        } finally {
//        }
//    }
//}
//
//
//
//
