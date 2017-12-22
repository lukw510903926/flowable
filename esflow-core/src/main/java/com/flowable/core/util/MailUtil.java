package com.flowable.core.util;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class MailUtil implements Serializable{
	
	private static Logger logger = Logger.getLogger("mailUtil");
	
	private static final long serialVersionUID = 1L;

	/**
	 * 邮箱账户
	 */
	private String userName;

	/**
	 * 发送者
	 */
    private String name;

    /**
     * 邮箱密码
     */
    private String password;

    /**
     * 邮件主题
     */
    private String subject;

    private String sender;

    /**
     * 邮件接收人
     */
    private String receiver;

    /**
     * 邮箱主体内容
     */
    private String message;

    /**
     * 邮件服务器
     */
    private String host;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
    
	public void sendEmail(List<String> receivers,String subject,String message) throws EmailException {

		if(receivers != null && !receivers.isEmpty()){
			for (String receiver : receivers) {
				try {
					HtmlEmail email = new HtmlEmail();
					email.setCharset("UTF-8");
					email.setFrom(userName, userName);
					email.setAuthentication(userName, password);
					email.setSubject(subject);
					email.setMsg(message);
					email.setHostName(host);
					email.addTo(receiver);
					logger.info("send email--------");
					email.send();
				} catch (Exception e) {
					logger.info(" 邮件发送失败 ,接收账户: " + receiver +" ---> error :" + e.getLocalizedMessage());
				}
			}
		}
	}
}
