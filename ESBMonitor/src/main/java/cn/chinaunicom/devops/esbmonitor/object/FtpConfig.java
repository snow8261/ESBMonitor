package cn.chinaunicom.devops.esbmonitor.object;

public class FtpConfig {

	public FtpConfig(String ipaddress, int port, String username, String password, String remote) {
		this.ipaddress = ipaddress;
		this.port = port;
		this.username = username;
		this.password = password;
		this.remote = remote;
	}

	

	private String ipaddress;
	private int port;
	private String username;
	private String password;
	private String remote;

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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
	
	@Override
	public String toString() {
		return "FtpServer [ipaddress=" + ipaddress + ", port=" + port + ", username=" + username + ", password="
				+ password + ", remote=" + remote + "]";
	}
}
