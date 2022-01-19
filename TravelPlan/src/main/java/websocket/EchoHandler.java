package websocket;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
//스프링 : @Component 어노테이션으로 해당 클래스가 객체화. 객체를 구분 할 수 있는 id값 설정
//			class EchoHandler 객체화시 기본적으로 id=echoHandler으로 설정됨
@Component //객체화. 웹소켓을 위한 서버 객체
public class EchoHandler extends TextWebSocketHandler implements InitializingBean{
	//TextWebSocketHandler : 문자 채팅을 위한 클래스
	//Set : 중복 불가 객체 모음
	//WebSocketSession : 클라이언트 접속 객체
	private Set<WebSocketSession> clients = new HashSet<WebSocketSession>();
	
	@Override
	public void afterPropertiesSet() throws Exception {}
	
	@Override //클라이언트 접속 완료 후 자동 실행되는 메서드
	public void afterConnectionEstablished(WebSocketSession session) throws Exception{
		//session : 클라이언트 한개. 접속 요청한 클라이언트
		super.afterConnectionEstablished(session); //클라이언트와 접속 완료
		System.out.println("클라이언트 접속:" + session.getId());
		clients.add(session); //클라이언트를 Set에 추가.
	}
		
	@Override //클라이언트에서 송신된 메세지를 서버가 수신한 경우 호출
	/*
	 * session : 송신한 클라이언트 접속 객체
	 * message : 슈신된 메세지 내용
	 */
	public void handleMessage(WebSocketSession session, 
			WebSocketMessage<?> message) throws Exception{
		String loadMessage = (String)message.getPayload();//수신된 메세지 문자열
		System.out.println(session.getId() + ":클라이언트 메세지 :" + loadMessage);
		clients.add(session); //등록된 session인 경우는 다시 추가 되지 않음
		//clients : 서버에 접속한 클라이언트 목록
		for(WebSocketSession s : clients) {
			s.sendMessage(new TextMessage(loadMessage));//수신된 메세지 송신
		}
	}
		
	@Override //오류 발생시 호출되는 메서드
	public void handleTransportError(WebSocketSession session, 
			Throwable exception) throws Exception{
		super.handleTransportError(session, exception);
		System.out.println("오류발생:"+exception.getMessage());
	}
	@Override//연결이 종료된 경우
	public void afterConnectionClosed(WebSocketSession session, 
			CloseStatus status) throws Exception{
		super.afterConnectionClosed(session, status);
		System.out.println("클라이언트 접속 해제:"+ status.getReason());
		clients.remove(session); //연결 종료된 클라이언트 Set에서 제거
	}
	
		
}
	


