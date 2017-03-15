package cn.chinaunicom.devops.esbmonitor;

public interface FtpInterface {
	
	public String fireOnConnectSuccess();
	
	public String fireOnConnectFail();
	
	public String fireOnLoginSuccess();
	
	public String fireOnLoginFail();
	
	public String[] fireOnListNames();
}
