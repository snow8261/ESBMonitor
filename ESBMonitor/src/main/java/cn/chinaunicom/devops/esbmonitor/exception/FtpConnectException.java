package cn.chinaunicom.devops.esbmonitor.exception;

public class FtpConnectException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4493490922745744322L;

	public FtpConnectException() {
	}

	public FtpConnectException(String arg0) {
		super(arg0);
	}

	public FtpConnectException(Throwable arg0) {
		super(arg0);
	}

	public FtpConnectException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FtpConnectException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
