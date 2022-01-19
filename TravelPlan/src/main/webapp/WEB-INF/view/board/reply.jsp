<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%-- /webapp/WEB-INF/view/board/reply.jsp --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>게시판 답글 쓰기</title></head><body>
<form:form modelAttribute="board" action="reply"   method="post" name="f">
  <%-- path=num => html 태그
  		<input type='hidden' id='num' name='num' value='board.getNum()값이 출력'
   --%>
  <form:hidden  path="num" />
    <%-- path=grp => html 태그
  		<input type='hidden' id='grp' name='grp' value='board.getGrp()값이 출력'
   --%>
  <form:hidden  path="grp" />
  <form:hidden  path="grplevel" />  <form:hidden  path="grpstep" />
  <table><caption>Spring 게시판 답글 등록</caption>
  <tr><td>글쓴이</td><td><input type="text" name="name">
    <font color="red"><form:errors path="name" /></font></td></tr>
  <tr><td>비밀번호</td><td><form:password path="pass" />
    <font color="red"><form:errors path="pass" /></font></td></tr>
  <tr><td>제목</td><td><form:input path="subject" value="RE:${board.subject}"/> 
  <font color="red"><form:errors path="subject" /></font></td></tr>
  <tr><td>내용</td><td><textarea name="content" rows="15" cols="80"></textarea>
  <script type="text/javascript">
  	CKEDITOR.replace("content",{filebrowserImageUploadUrl : "imgupload"});
  </script>
  <font color="red"><form:errors path="content" /></font></td></tr>
  <tr><td colspan="2">
  <a href="javascript:document.f.submit()">[답변글등록]</a></td></tr>    
  </table></form:form></body></html>