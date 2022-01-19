<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>    
<%-- /WEB-INF/view/chat/chat.jsp : 웹채팅 프로그램 --%>    
<!DOCTYPE><html><head><meta charset="UTF-8">
<title>websocket client</title>
<c:set var="port" value="${pageContext.request.localPort}"/><%--8090 --%>
<c:set var="server" value="${pageContext.request.serverName}"/><%--localhost --%>
<c:set var="path" value="${pageContext.request.contextPath}"/><%--springstudy2 --%>
<script type="text/javascript">
	$(function(){ 
								//ws://localhost:8090/springsyduy2/chat/chatting
		var ws = new WebSocket("ws://${server}:${port}${path}/chatting");
		ws.onopen = function(){ //서버 연결 완료
			$("#chatStatus").text("info: connection opened")
			$("input[name=chatInput]").on("keydown",function(evt){
				if(evt.keyCode == 13){ //엔터키 눌린 경우
					var msg = $("input[name=chatInput]").val();
					ws.send(msg);//웹소켓으로 입력한 데이터를 서버로 전송
					$("input[name=chatInput]").val("");
				}
			})
		}
		ws.onmessage = function(event){ //웹소켓을 통해 서버로 부터 데이터 수신
			//append : 뒤에 추가
			//prepend : 앞쪽에 추가
			//event.data : 서버에서 전송된 메세지. 수신된 메세지
			$("textarea").eq(0).append(event.data+"\n");  //새로운 채팅이 아래쪽으로
		}
		//서버와 연결 끊어 진 경우
		ws.onclose = function(event){
			$("#chatStatus").text("info:connection close");
		}
	})

</script></head>
<body>
<p><div id="chatStatus"></div>
<textarea name="chatMsg" rows="15" cols="40"></textarea><br>
메세지 입력 : <input type="text" name="chatInput">
</body>
</html>