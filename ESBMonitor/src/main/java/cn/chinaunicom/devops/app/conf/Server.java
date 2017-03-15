package cn.chinaunicom.devops.app.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("Server")
public class Server {
	
	
	private String ip;
	private String port;
	private String username;
	private String password;
	private String potocol;
	private String name;
	private String owner;
	private String user;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getPotocol() {
		return potocol;
	}
	public void setPotocol(String potocol) {
		this.potocol = potocol;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Server [name=" + name + ", ip=" + ip + ", port=" + port + ", potocol=" + potocol + ", username="
				+ username + ", password=" + password + ", owner=" + owner + ", user=" + user + "]";
	}

	

}
