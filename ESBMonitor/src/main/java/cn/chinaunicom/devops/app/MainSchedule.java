package cn.chinaunicom.devops.app;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MainSchedule {

    private static final Logger logger = LoggerFactory.getLogger(MainSchedule.class);
    
    @Autowired 
    private FTPMonitorTask ftpmonitortask;
    
     //59 55 0/1 * * ?
    @Scheduled(cron="59 55 4-22 * * ?") 
    public void executeHourFileCheckTask() {

        // 间隔1小时,执行工单上传任务     
        Thread current = Thread.currentThread();  
        System.out.println("每小时数据检查:"+current.getId()+" :"+LocalDateTime.now());
        logger.info("ScheduledTest.executeFileDownLoadTask 定时任务1:"+current.getId()+ ",name:"+current.getName());
        ftpmonitortask.checkHourlyTask();
    }

    @Scheduled(cron="0 0 8 1/1 * ?") 
    public void executeDailyFileCheckTask() {

        // 间隔1分钟,执行工单上传任务              
        Thread current = Thread.currentThread();  
        System.out.println("每天数据检查:"+current.getId());
        logger.info("ScheduledTest.executeUploadTask 定时任务2:"+current.getId() + ",name:"+current.getName());
    }

    @Scheduled(cron="0 0/10 * * * ?") 
    public void executeSystemCheckTask() {

        // 间隔3分钟,执行工单上传任务                          
        Thread current = Thread.currentThread();  
        System.out.println("系统正常:"+current.getId()+" :"+LocalDateTime.now());
        logger.info("ESBMonitor 系统正常:"+current.getId()+ ",name:"+current.getName());
    }    

    
    @Scheduled(cron="0 0/5 * * * ?") 
    public void executeTestTask() {

        // 间隔5分钟,执行工单上传任务                          
        Thread current = Thread.currentThread();  
        System.out.println("开始测试:"+current.getId());
        logger.info("ESBMonitor 测试:"+current.getId()+ ",name:"+current.getName());
        
    } 
    
}