package cn.chinaunicom.devops.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.chinaunicom.devops.app.conf.Task;
@Component
public class FTPMonitorTask {

    @Autowired
    private MainConfiguration mainConfiguration;
    @Autowired
    private FTPMonitorJob ftpmonitorjob;
    public void checkWeeklyTask(){}
    public void checkDailyTask(){}
    public void testTask(){
    	List<Task> tasks=mainConfiguration.getTasks();
    	Task task=tasks.get(1);
    	ftpmonitorjob.start(task);
    }
    
}
