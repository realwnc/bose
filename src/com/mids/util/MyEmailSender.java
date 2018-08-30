package com.mids.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mids.util.MyEmailUtil.MyAuthenticator;

/**
 * 邮件管理器
 * java 实现邮件的发送， 抄送及多附件
 * @author zhuxiongxian
 * @version 1.0
 * @created at 2016年10月8日 下午3:52:11
 */
public class MyEmailSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyEmailUtil.class);
	private static final String PROPERTIES_FILE_PATH = "/email.properties";
	// 邮件发送协议  
    private static String PROTOCOL = "smtp";
    // SMTP邮件服务器  
    private static String HOST = "smtp.163.com";
    // SMTP邮件服务器默认端口  
    private static String PORT = "25";
    // 发件人  
    private static String FROM = "meijiajiang2016@163.com";
    // 是否要求身份认证  
    private static String IS_AUTH = "true";
    private static String USERNAME = "meijiajiang2016";
    private static String PASSWORD = "";
    // 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）  
    private static String IS_ENABLED_DEBUG_MOD = "true";
    //发件人昵称
    private static String senderNick = "广东移动";   // 发件人昵称
    // 初始化连接邮件服务器的会话信息  
    private static Properties props = null;
    
    //private Session session; // 邮件会话对象 
    //private MimeMessage mimeMsg; // MIME邮件对象 
    //private Multipart mp;   // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象 

    private static MyEmailSender instance = null; 

    public MyEmailSender() {
    	InputStream in = null;
        try {
            in = MyEmailUtil.class.getResourceAsStream(PROPERTIES_FILE_PATH);
            Properties propertie = new Properties();
            propertie.load(in);
            PROTOCOL = propertie.getProperty("protocol");
            HOST = propertie.getProperty("host");
            PORT = propertie.getProperty("port");
            FROM = propertie.getProperty("from");
            IS_AUTH = propertie.getProperty("auth");
            USERNAME = propertie.getProperty("username");
            PASSWORD = propertie.getProperty("password");
            
            props = new Properties();
            props.setProperty("mail.transport.protocol", PROTOCOL);
            props.setProperty("mail.smtp.host", HOST);
            props.setProperty("mail.smtp.port", PORT);
            props.setProperty("mail.smtp.auth", IS_AUTH);
            props.setProperty("mail.debug",IS_ENABLED_DEBUG_MOD);            
        }
        catch (Exception e) {
            LOGGER.error("-------->error on read config="+PROPERTIES_FILE_PATH);
            e.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                	LOGGER.error("-------->error on close config="+PROPERTIES_FILE_PATH);
                	e.printStackTrace();
                }
            }
        }
        
    }

    public static MyEmailSender getInstance() {
        if (instance == null) {
            instance = new MyEmailSender();
        }
        return instance; 
    }

    /**
     * 发送邮件
     * @param from 发件人
     * @param to 收件人
     * @param copyto 抄送
     * @param subject 主题
     * @param content 内容
     * @param fileList 附件列表
     * @return
     */
    public boolean sendMail(String[] to, String[] copyto, String subject, String content, String[] fileList) {
        boolean success = true;
        try {
        	// 建立会话
            Session session = Session.getDefaultInstance(props, new MyAuthenticator());
            session.setDebug(false);
            MimeMessage mimeMsg = new MimeMessage(session);
            Multipart mp = new MimeMultipart(); 

            // 自定义发件人昵称
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText(senderNick);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 设置发件人
//          mimeMsg.setFrom(new InternetAddress(from));
            mimeMsg.setFrom(new InternetAddress(FROM, nick));
            // 设置收件人
            if (to != null && to.length > 0) {
                String toListStr = getMailList(to);
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toListStr));
            }
            // 设置抄送人
            if (copyto != null && copyto.length > 0) {
                String ccListStr = getMailList(copyto);
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccListStr)); 
            }
            // 设置主题
            mimeMsg.setSubject(subject);
            // 设置正文
            BodyPart bp = new MimeBodyPart(); 
            bp.setContent(content, "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            // 设置附件
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    bp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(fileList[i]); 
                    bp.setDataHandler(new DataHandler(fds)); 
                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                    mp.addBodyPart(bp); 
                }
            }
            mimeMsg.setContent(mp); 
            mimeMsg.saveChanges(); 
            // 发送邮件
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp"); 
                transport.connect((String)props.get("mail.smtp.host"), (String)props.get("username"), (String)props.get("password")); 
//              transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO)); 
//              transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                transport.close(); 
            } else {
                Transport.send(mimeMsg);
            }
            System.out.println("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * 发送邮件
     * @param from 发件人
     * @param to 收件人, 多个Email以英文逗号分隔
     * @param cc 抄送, 多个Email以英文逗号分隔
     * @param subject 主题
     * @param content 内容
     * @param fileList 附件列表
     * @return
     */
    public boolean sendMail(String to, String cc, String subject, String content, String[] fileList) {
        boolean success = true;
        try {
        	Session session = Session.getDefaultInstance(props, new MyAuthenticator());
            session.setDebug(false);
            MimeMessage mimeMsg = new MimeMessage(session);
            Multipart mp = new MimeMultipart(); 

            // 自定义发件人昵称
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText(senderNick);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 设置发件人
//          mimeMsg.setFrom(new InternetAddress(from));
            mimeMsg.setFrom(new InternetAddress(FROM, nick));
            // 设置收件人
            if (to != null && to.length() > 0) {
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }
            // 设置抄送人
            if (cc != null && cc.length() > 0) {
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc)); 
            }
            // 设置主题
            mimeMsg.setSubject(subject);
            // 设置正文
            BodyPart bp = new MimeBodyPart(); 
            bp.setContent(content, "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            // 设置附件
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    bp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(fileList[i]); 
                    bp.setDataHandler(new DataHandler(fds)); 
                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                    mp.addBodyPart(bp); 
                }
            }
            mimeMsg.setContent(mp); 
            mimeMsg.saveChanges(); 
            // 发送邮件
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp"); 
                transport.connect((String)props.get("mail.smtp.host"), (String)props.get("username"), (String)props.get("password")); 
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                transport.close(); 
            } else {
                Transport.send(mimeMsg);
            }
            System.out.println("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public String getMailList(String[] mailArray) {
        StringBuffer toList = new StringBuffer();
        int length = mailArray.length;
        if (mailArray != null && length < 2) {
            toList.append(mailArray[0]);
        } else {
            for (int i = 0; i < length; i++) {
                toList.append(mailArray[i]);
                if (i != (length - 1)) {
                    toList.append(",");
                }

            }
        }
        return toList.toString();
    }
    
    /**
     * 向邮件服务器提交认证信息
     */
    static class MyAuthenticator extends Authenticator {
        private String username = USERNAME;
        private String password =  PASSWORD;
   
        public MyAuthenticator() {
            super();
        }
        public MyAuthenticator(String username, String password) {
            super();
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
    

    public static void main(String[] args) {
        String from = "123@qq.com";
        String[] to = {"10086@qq.com", "xx@zhuxiongxian.cc"};
        String[] copyto = {"123456@163.com"};
        String subject = "测试一下";
        String content = "这是邮件内容，仅仅是测试，不需要回复.";
        String[] fileList = new String[3];
        fileList[0] = "d:/zxing.png";
        fileList[1] = "d:/urls.txt";
        fileList[2] = "d:/surname.txt";
        MyEmailSender.getInstance().sendMail(to, copyto, subject, content, fileList);
    }
}