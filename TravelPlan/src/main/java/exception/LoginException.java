package exception;

public class LoginException extends RuntimeException {
	private String url;
	public LoginException (String msg,String url) {  //생성자
		super(msg);
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
}
