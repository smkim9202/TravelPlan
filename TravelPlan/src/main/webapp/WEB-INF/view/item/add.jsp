<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/item/add.jsp --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>상품 등록 화면</title></head>
<%--enctype="multipart/form-data" form 인경우 method="post" 설정
     commons-fileupload 툴을 이용하여 파일 업로드함.
--%>
<body><form:form  modelAttribute="item" action="register"
   enctype="multipart/form-data">
   <h2>상품 등록</h2><table><tr><td>상품명</td>
     <td><form:input path="name" maxlength="20" /></td>
     <td><font color="red"><form:errors path="name" /></font></td>
   </tr><tr><td>상품가격</td>
     <td><form:input path="price" maxlength="20" /></td>
     <td><font color="red"><form:errors path="price" /></font></td>
   </tr><tr><td>상품이미지</td>
     <td colspan="2"><input type="file" name="picture"></td>
   </tr><tr><td>상품설명</td>
   <td><form:textarea path="description" cols="20" rows="5" /></td>
 <td><font color="red"><form:errors path="description" /></font></td>
   </tr><tr><td colspan="3"><input type="submit" value="상품등록">&nbsp;
<input type="button" value="상품목록" onclick="location.href='list'">
   </td></tr></table></form:form></body></html>