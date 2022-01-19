package exception;
//RuntimeException : 예외처리 생략가능 
public class BoardException extends RuntimeException {
	private String url;
	public BoardException (String msg,String url) {  //생성자
		super(msg);
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
}
/*
 *        Throwable
 * Exception      Error
 *     |  
 *  IOException
 *  InterruptedException
 *  RuntimeException -> getMessage() 메서드 
 *    ...
 *        => 예외는 RuntimeException 과 그외 Exception으로 나눈다.
 *           RuntimeException : 예외 생략 가능        
*/
