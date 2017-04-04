package cn.chinaunicom.devops.app;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.chinaunicom.devops.app.conf.Server;
import cn.chinaunicom.devops.app.conf.Task;
import cn.chinaunicom.devops.app.smsservice.SMSMessage;
import cn.chinaunicom.devops.app.smsservice.SMSSender;
import cn.chinaunicom.devops.esbmonitor.object.FtpConfig;

@Component
public class FTPMonitorJob {
	@Autowired
	private MainConfiguration mainConfiguration;
	
	private  Pattern pattern = Pattern.compile("_(20\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2})");
	private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Task task;

	public FTPMonitorJob(Task task) {
		this.task = task;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void start(Task task) {
		String source = task.getSource();
		Server server = mainConfiguration.getServerByName(source);
		String path = task.getPath();
		
		String owner = server.getOwner();
		String user=server.getUser();
		List<String> files = listRemoteFiles(server, path,0);
		List<SMSMessage> ms = new ArrayList<SMSMessage>();
		SMSMessage mess = new SMSMessage();
		mess.setContent("files 个数" + files.size());
		mess.setPhoneNumber("18601720015");
		ms.add(mess);
		SMSSender sender = new SMSSender();
		System.out.println(mess.toString());
		sender.sendSMS(ms);
	}

	public List<String> checkNullFiles(Server ftpserver, String path) {
		FTPClient ftp = new FTPClient();
		FTPClientConfig config = new FTPClientConfig();
		ftp.configure(config);
		boolean error = false;
		List<String> lines = new ArrayList<String>();
		try {
			int reply;
			String server = ftpserver.getIp();
			int port = Integer.parseInt(ftpserver.getPort());
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
				FTPFile[] files = ftp.listFiles(path, new FTPFileFilter() {
					@Override
					public boolean accept(FTPFile file) {
						if(file.getName().endsWith("tmp")){
							return false;
						}
						
						return  file.getSize()==0;
					}
				});
				for (FTPFile ftpfile : files) {
					lines.add(ftpfile.getName());
				}
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
					ioe.printStackTrace();
				}
			}
		}
		return lines;
	}
	
	public List<String> listRemoteFiles(Server ftpserver, String path,int delay) {
		FTPClient ftp = new FTPClient();
		FTPClientConfig config = new FTPClientConfig();
		ftp.configure(config);
		boolean error = false;
		List<String> lines = new ArrayList<String>();
		try {
			int reply;
			String server = ftpserver.getIp();
			int port = Integer.parseInt(ftpserver.getPort());
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
				FTPFile[] files = ftp.listFiles(path, new FTPFileFilter() {
					@Override
					public boolean accept(FTPFile file) {
						LocalDateTime today = LocalDateTime.now();
						
						LocalDateTime now1hour = today.minusHours(1);
						Calendar fileTime = file.getTimestamp();
						Date ft = fileTime.getTime();
						LocalDateTime filedate = ft.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
						boolean timecheck=now1hour.isBefore(filedate);
						if(delay<=0){
							return timecheck;
						}else {
							LocalDateTime nowdelay=today.minusMinutes(delay);
							String filename=file.getName();
							Date frealtime= getDatefromFileName(filename);
							LocalDateTime filerdate = frealtime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//							if(timecheck&& nowdelay.isBefore(filerdate)){
//								System.out.println(filename);
//							}
							return  timecheck&& nowdelay.isBefore(filerdate);
						}
					}

					private Date getDatefromFileName(String filename) {
						Date d=null;
						Matcher matcher = pattern.matcher(filename);
						if (matcher.find()) {
							String datestr= matcher.group(1);
							 try {
								d= df.parse(datestr);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}else {
						    System.err.println("Match not found "+ filename);
						}
						return d;
					}
				});
				for (FTPFile ftpfile : files) {
					lines.add(ftpfile.getName());
				}
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
					ioe.printStackTrace();
				}
			}
		}
		return lines;
	}
}
