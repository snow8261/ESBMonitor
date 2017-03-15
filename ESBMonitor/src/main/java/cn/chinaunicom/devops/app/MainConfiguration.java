package cn.chinaunicom.devops.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import cn.chinaunicom.devops.app.conf.Server;
import cn.chinaunicom.devops.app.conf.Task;
import cn.chinaunicom.devops.app.conf.User;

@ConfigurationProperties(prefix="myProps")
@Component
public class MainConfiguration implements InitializingBean {
	 private List<User> users = new ArrayList<User>();
	 private List<Server> servers=new ArrayList<Server>();
	 private List<Task> tasks=new ArrayList<Task>();
	 private Map<String,Server> name2server=new HashMap<>();
	 private Map<String,User> name2user= new HashMap<>();
	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Server> getServers() {
		return servers;
	}

	public void setServers(List<Server> servers) {
		this.servers = servers;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {
		for(Server server:getServers()){
			name2server.put(server.getName(), server);
		}
		for(User user:getUsers()){
			name2user.put(user.getName(), user);
		}
	}
	
	public Server getServerByName(String name){
		return name2server.get(name);
	}
	
	//public 
	
}
