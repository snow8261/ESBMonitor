package cn.chinaunicom.devops.app.smsservice;

import java.io.Serializable;

public class SMSMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6973058891645878772L;
	private String Content;
	private String phoneNumber;

	public SMSMessage() {
	}

	@Override
	public String toString() {
		return "SMSMessage [Content=" + Content + ", phoneNumber=" + phoneNumber + "]";
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
