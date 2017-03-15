package cn.chinaunicom.devops.app.smsservice;


import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;
import org.tempuri.SendSmsServiceLocator;
import org.tempuri.SendSmsServiceSoap;

public class SMSSender {

	private void send(String content, String mobileList) {
		SendSmsServiceLocator sssl = new SendSmsServiceLocator();
		try {
			SendSmsServiceSoap sss = sssl.getSendSmsServiceSoap();
			System.out.println("send! "+content+" :"+" mob:"+mobileList);
			sendWordLessThan69(sss, content, mobileList);
		} catch (ServiceException ex) {
			ex.printStackTrace();
			System.err.println("send err "+content+" :"+" mob:"+mobileList);
		}
	}

	private void sendWordLessThan69(SendSmsServiceSoap sss, String content,
			String mobileList) {
		if (content.length() > 69) {
			String sendmessage = content.substring(0, 69);
			try {
				sss.sendSms(sendmessage, mobileList, "");
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			sendmessage = content.substring(69);
			sendWordLessThan69(sss, sendmessage, mobileList);
		} else {
			try {
				sss.sendSms(content, mobileList, "");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendSMS(List<SMSMessage> messages) {
		if (messages == null) {
			return;
		}
		for (SMSMessage smsMessage : messages) {

			send(smsMessage.getContent(), smsMessage.getPhoneNumber());
		}
	}
	
	
}
