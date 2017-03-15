package cn.chinaunicom.devops.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import cn.chinaunicom.devops.esbmonitor.object.FtpConfig;

public class FtpMonitor {

	public void listRemoteFiles(FtpConfig ftpserver, String path) {
		FTPClient ftp = new FTPClient();
		FTPClientConfig config = new FTPClientConfig();
		ftp.configure(config);
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

	public static void main(String[] args) {
		// "-n 172.25.21.5:811 ftpuser5 oss20Ffp5 /LTE/MOBILE/HUAWEI/OMC1/XDR"
		FtpConfig ftpserver = new FtpConfig("172.25.21.5", 811, "ftpuser5", "oss20Ffp5","");
		String path = "/LTE/MOBILE/HUAWEI/OMC1/XDR";
		FtpMonitor fp = new FtpMonitor();
		fp.listRemoteFiles(ftpserver, path);
	}
}
