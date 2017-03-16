package cn.chinaunicom.devops.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.chinaunicom.devops.app.conf.Server;
import cn.chinaunicom.devops.app.conf.Task;
import cn.chinaunicom.devops.app.smsservice.SMSMessage;
import cn.chinaunicom.devops.app.smsservice.SMSSender;
@Component
public class FTPMonitorTask {
    public static final int PMAlarm= 20; 
    @Autowired
    private MainConfiguration mainConfiguration;
    @Autowired
    private FTPMonitorJob ftpmonitorjob;
    public void checkHourlyTask(){
    	List<Task> tasks=mainConfiguration.getTasks();
    	for(Task task:tasks){
    		if(task.getPeriod().equals("hourly")){
    			String source = task.getSource();
    			Server server = mainConfiguration.getServerByName(source);
    			String path = task.getPath();
    			LocalDateTime today = LocalDateTime.now();
    			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    			String ftoday=today.format(formatter);
    			List<String> files = ftpmonitorjob.listRemoteFiles(server, path+"/"+ftoday);
    			int threshold=Integer.valueOf(task.getThreshold());
   			    if(files.size()>threshold)   
    			   return ;
    			String taskname=task.getNamecn();
    			String mess = taskname+ " 上报数量 "+ files.size()+" < 阀值 "+threshold+" 请核查！" ;
    			sendMess(server, mess);
    		}
    	}
    }


	private void sendMess(Server server, String message) {
		String user=server.getUser();
		String[] username=user.split(",");
		List<SMSMessage> ms = new ArrayList<SMSMessage>();
		for(String usr:username){
			SMSMessage mess = new SMSMessage();
			mess.setContent(message);
			String num=mainConfiguration.getUserbyName(usr).getPhone();
			mess.setPhoneNumber(num);
			ms.add(mess);
		}
		SMSSender sender = new SMSSender();
		sender.sendSMS(ms);
	}
  
    
    public void checkDailyTask(){}
    public void testTask(){
    	checkHourlyTask();
    }
    
}
