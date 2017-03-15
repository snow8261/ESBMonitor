package hello;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

import cn.chinaunicom.devops.esbmonitor.object.FtpConfig;

public class FtpMonitorExample {

	public List<String> listRemoteFiles(FtpConfig ftpserver, String path) {
		FTPClient ftp = new FTPClient();
		FTPClientConfig config = new FTPClientConfig();
		ftp.configure(config);
		ftp.configure(config);
		boolean error = false;
		List<String> lines=new ArrayList<String>();
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
				//String[] strings = ftp.listNames(path);
				FTPFile[] files=ftp.listFiles(path,new FTPFileFilter() {
					        @Override
					        public boolean accept(FTPFile file) {
					        	 LocalDateTime today = LocalDateTime.now();
					        	 today= today.minusHours(1);
					        	 System.err.println(today);
//					        	 Instant in=today.toInstant(ZoneId.systemDefault());
//					        	 long ins=in.toEpochMilli();
					        	 Calendar  fileTime=file.getTimestamp();
					        	 Date ft=fileTime.getTime();
					        	 LocalDateTime filedate = ft.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//					        	 long time=fileTime.getTime().getTime();
//					        	 System.err.println("ins."+ins+": time"+time);
					        	// LocalDateTime
					        	 System.err.println(filedate);
					        	return today.isBefore(filedate);
					      //    return false;  
					        }
					    }
				);
				for(FTPFile ftpfile:files){
					lines.add(ftpfile.getName()+" size:"+ftpfile.getSize()+" time:"+ftpfile.getTimestamp());
				}
		      //  lines = new ArrayList<String>(Arrays.asList(strings));
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
		return lines;
	}

	public static void main(String[] args) {
		// "-n 172.25.21.5:811 ftpuser5 oss20Ffp5 /LTE/MOBILE/HUAWEI/OMC1/XDR"
		FtpConfig ftpserver = new FtpConfig("10.161.96.3", 811, "ftpuser5", "oss20Ffp5","");
		String path = "/LTE/MOBILE/HUAWEI/OMC1/XDR/20170314";
		FtpMonitorExample fp = new FtpMonitorExample();
		fp.listRemoteFiles(ftpserver, path);
	}
}
