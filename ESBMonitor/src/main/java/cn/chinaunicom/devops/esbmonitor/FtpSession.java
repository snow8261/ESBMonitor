package cn.chinaunicom.devops.esbmonitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import cn.chinaunicom.devops.esbmonitor.exception.FtpConnectException;
import cn.chinaunicom.devops.esbmonitor.exception.FtpLoginException;
import cn.chinaunicom.devops.esbmonitor.object.FtpConfig;

public class FtpSession {

	private FtpConfig ftpserver;

	private FtpListener ftpListener;

	private String server;

	private int port;

	private String username;

	private String password;

	private FTPClient ftp;

	private FTPClientConfig config;

	private boolean isConnected;
	
	public FtpSession(FtpConfig ftpserver, FtpListener ftpListener) {
		this.ftpserver = ftpserver;
		this.ftpListener = ftpListener;
	}

	private void init() {
		server = ftpserver.getIpaddress();
		port = ftpserver.getPort();
		username = ftpserver.getUsername();
		password = ftpserver.getPassword();
	}

	public void doWork() {
		try {
			init();
			connect();
			login();
			work();
		} catch (FtpConnectException e) {
			ftpListener.fireOnConnectFail();
		} catch (FtpLoginException e) {
			ftpListener.fireOnLoginFail();
		} finally {
			disconnect();
		}
	}

	private void disconnect() {
		if (ftp.isConnected()) {
			try {
				ftp.disconnect();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}

	private void work() {
		// TODO Auto-generated method stub

	}

	private void login() throws FtpLoginException {
		try {
			boolean isLogin=ftp.login(username, password);
			if(isLogin){
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new FtpLoginException(ftpserver+" "+e.getMessage());
		}
	}

	private void connect() throws FtpConnectException {
		ftp = new FTPClient();
		config = new FTPClientConfig();
		ftp.configure(config);
		try {
			if (port > 0) {
				ftp.connect(server, port);
			} else {
				ftp.connect(server);
			}
			System.out.println("Connected to " + server + ".");
			System.out.print(ftp.getReplyString());
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.err.println("FTP server refused connection.");
				throw new FtpConnectException(ftpserver+" FTP server refused connection.");
			}
			ftp.enterLocalPassiveMode();
			ftpListener.fireOnConnectSuccess();
			isConnected=true;
		} catch (IOException e) {
			e.printStackTrace();
			throw new FtpConnectException(ftpserver+" "+e.getMessage());
		} 
	}

	public void listRemoteFiles(FtpConfig ftpserver, String path) {
		FTPClient ftp = new FTPClient();
		FTPClientConfig config = new FTPClientConfig();
		ftp.configure(config);
		boolean error = false;
		try {
			int reply;
			String server = ftpserver.getIpaddress();
			int port = ftpserver.getPort();
			if (port > 0) {
				ftp.connect(server, port);
			} else {
				ftp.connect(server);
			}
			System.out.println("Connected to " + server + ".");
			System.out.print(ftp.getReplyString());
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.err.println("FTP server refused connection.");
			}
			ftp.enterLocalPassiveMode();

			if (ftp.login(ftpserver.getUsername(), ftpserver.getPassword())) {
				System.out.println("Remote system is " + ftp.getSystemType());
				String[] strings = ftp.listNames(path);
				
				List<String> lines = new ArrayList<String>(Arrays.asList(strings));
				FileUtils.writeLines(new File("D:\\name,.txt"), lines);
				ftp.noop();
			} else {
				error = true;
			}
			ftp.logout();
		} catch (IOException e) {
			error = true;
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					// do nothing
				}
			}
		}
	}

//	 public static void main(String[] args) {
//	 // "-n 172.25.21.5:811 ftpuser5 oss20Ffp5 /LTE/MOBILE/HUAWEI/OMC1/XDR"
//	 FtpServer ftpserver = new FtpServer("172.25.21.5", 811, "ftpuser5",
//	 "oss20Ffp5");
//	 String path = "/LTE/MOBILE/HUAWEI/OMC1/XDR";
//	 FtpInstance fp = new FtpInstance();
//	 fp.listRemoteFiles(ftpserver, path);
//	 }
}
