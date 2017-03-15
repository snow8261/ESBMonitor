package cn.chinaunicom.devops.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class MainController {
    

    @Value("${app.msg}")
    private String msg;
    
    @Autowired
    private MainConfiguration mainConfiguration;
    
    @Autowired
    private FTPMonitorTask ftpmonitortask;
    

    @RequestMapping("/")
    public String index() throws JsonProcessingException {
    	ObjectMapper objectMapper=new ObjectMapper();
        return msg+" "+objectMapper.writeValueAsString(mainConfiguration.getUsers()+" --"
        		+objectMapper.writeValueAsString(mainConfiguration.getServers())+"--"
        		+objectMapper.writeValueAsString(mainConfiguration.getTasks()));
    }
    
    @RequestMapping("/server")
    public String server() throws JsonProcessingException {
    	return mainConfiguration.getServerByName("wx_ftp")+"";
    }
    
    @RequestMapping("/test")
    public String test() throws JsonProcessingException {
    	 ftpmonitortask.testTask();
    	 return "send";
    }
}
