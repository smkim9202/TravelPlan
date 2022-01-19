package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import websocket.EchoHandler;


@Configuration
@EnableWebSocket //웹소켓 사용
public class WebSocketConfig implements WebSocketConfigurer{
	@Override //웹소켓 서버 설정
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new EchoHandler(), "chatting")
		.setAllowedOrigins("*");//모든 브라우저 허용
	}
}
