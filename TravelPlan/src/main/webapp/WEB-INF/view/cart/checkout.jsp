<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- /webapp/WEB-INF/view/cart/checkout.jsp
   1. checkout controller 클래스 구현.
   2. 로그인된 경우만 이페이지 조회가능
   3. 장바구니 상품이 있는 경우만 이페이지 조회가능(CartException 오류객체 생성. 예외 발생)
     => 2,3 aop 클래스(CartAspect) 구현하기
--%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>    
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>주문 전 상품 목록 보기</title></head>
<body><h2>배송지 정보</h2>
<table><tr><td width="30%">주문아이디</td>
      <td width="70%">${sessionScope.loginUser.userid}</td></tr>
  <tr><td width="30%">이름</td>
      <td width="70%">${sessionScope.loginUser.username}</td></tr>
  <tr><td width="30%">우편번호</td>
      <td width="70%">${sessionScope.loginUser.postcode}</td></tr>
  <tr><td width="30%">주소</td>
      <td width="70%">${sessionScope.loginUser.address}</td></tr>
  <tr><td width="30%">전화번호</td>
      <td width="70%">${sessionScope.loginUser.phoneno}</td></tr>
</table>
<h2>구매 상품 </h2>
<table><tr><th>상품명</th><th>가격</th><th>수량</th><th>합계</th></tr>
  <c:forEach items="${sessionScope.CART.itemSetList}"
      var="itemSet" varStatus="stat">
   <tr><td>${itemSet.item.name}</td><td>${itemSet.item.price}</td>
   <td>${itemSet.quantity}</td>
   <td>${itemSet.item.price *itemSet.quantity}</td></tr>
  </c:forEach>
  <tr><td colspan="4" align="right">
  <%-- cart.getTotal() : 전체 총 주문 금액 (수량*단가)+(수량*단가)... = 총주문금액 --%>
          총 구입 금액 : ${sessionScope.CART.total}원</td></tr>
  <tr><td colspan="4"><a href="end">주문확정</a>&nbsp;
     <a href="../item/list">상품 목록</a>&nbsp;
  </td></tr></table></body></html>