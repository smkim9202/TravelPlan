<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /webapp/layout/layout2.jsp --%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
<title><decorator:title /></title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-theme-black.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
html,body,h1,h2,h3,h4,h5,h6 {font-family: "Roboto", sans-serif;}
.w3-sidebar {
  z-index: 3;
  width: 420px;
  top: 43px;
  bottom: 0;
  height: inherit;
}
</style>
<script type="text/javascript" src=
"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript" 
   src="http://cdn.ckeditor.com/4.5.7/standard/ckeditor.js"></script>
<decorator:head />
<link rel="stylesheet" href="${path}/css/main.css">  
</head>
<body>
<!-- Navbar -->
<div class="w3-top">
  <div class="w3-bar w3-theme w3-top w3-left-align w3-large">
    <a class="w3-bar-item w3-button w3-right w3-hide-large w3-hover-white w3-large w3-theme-l1" href="javascript:void(0)" onclick="w3_open()"><i class="fa fa-bars"></i></a>
    <a href="${path }" class="w3-bar-item w3-button w3-theme-l1">TravelPlan</a>
    <a href="${path }/item/list" class="w3-bar-item w3-button  w3-hover-white">가이드북판매</a>
    <a href="${path }/board/list" class="w3-bar-item w3-button w3-hover-white">여행루트</a>
 	<a href="${path }/chat/chat" class="w3-bar-item w3-button  w3-hover-white">채팅</a>
 	<a href="${path }/user/main" class="w3-bar-item w3-button  w3-hover-white">회원</a>
 	
  <!--   <a href="#" class="w3-bar-item w3-button w3-hide-small w3-hide-medium w3-hover-white">Clients</a>
    <a href="#" class="w3-bar-item w3-button w3-hide-small w3-hide-medium w3-hover-white">Partners</a>-->
  </div>
</div>

<!-- Sidebar -->
<nav class="w3-sidebar w3-bar-block w3-collapse w3-large w3-theme-l5 w3-animate-left" id="mySidebar">
<div class="w3-bar-block">
  <a href="javascript:void(0)" onclick="w3_close()" class="w3-right w3-xlarge w3-padding-large w3-hover-black w3-hide-large" title="Close Menu">
    <i class="fa fa-remove"></i>
  </a>
  <h4 class="w3-bar-item"><b>환율정보</b></h4>
</div>
<div class="w3-container">
	<%--ajax을 통해 얻은 KEB하나은행 환율 정보 내용 출력 : 서버단에서 html --%>
	<div id="exchange"></div>
</div><br>
<div class="w3-container">
	<%--ajax을 통해 얻은 KEB하나은행 환율 정보 내용 출력 : JS로 html --%>
	<div id="exchange2"></div>
</div><br>
</nav>

<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<!-- Main content: shift it to the right by 250 pixels when the sidebar is visible -->
<div class="w3-main" style="margin-left:440px; margin-right:20px;">
	<div class="w3-row w3-padding-64">
		<decorator:body />
	</div>
</div>
  <footer id="myFooter">
    <div class="w3-container w3-theme-l2 w3-padding-32" style="margin-left:2%; margin-right:2%; text-align:right;">
      <h4>KIC 캠퍼스:인공지능을 활용한 웹플랫폼구축</h4>
      <p>kimsangme</p>
    </div>

    <div class="w3-container w3-theme-l1" style="margin-left:2%; margin-right:2%; text-align:right;">
      <p>Powered by <a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    </div>
  </footer>

<!-- END MAIN -->
</div>
<script type="text/javascript" 
src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js"></script>
<script>
// Get the Sidebar
var mySidebar = document.getElementById("mySidebar");

// Get the DIV with overlay effect
var overlayBg = document.getElementById("myOverlay");

// Toggle between showing and hiding the sidebar, and add overlay effect
function w3_open() {
  if (mySidebar.style.display === 'block') {
    mySidebar.style.display = 'none';
    overlayBg.style.display = "none";
  } else {
    mySidebar.style.display = 'block';
    overlayBg.style.display = "block";
  }
}

// Close the sidebar with the close button
function w3_close() {
  mySidebar.style.display = "none";
  overlayBg.style.display = "none";
}


//layout 화면에 환율 정보 데이터를 가져오기
$(function(){
	//서버에서 객체
	exchangeRate(); //하나은행 환율 정보(서버에서html)
	exchangeRate2(); //하나은행 환율 정보
})
//하나은행 : 서버에서 html 문장 작성을하여 화면에 출력
function exchangeRate(){
	$.ajax("${path}/ajax/exchange",{
		success : function(data){
			//data : html 형태의 text 문자열
			console.log(data)
			$("#exchange").html(data);
		},
		error: function(e){
			alert("환율 조회시 서버 오류:"+e.status);
		}
	})
}

//하나은행 : 서버에서 JSON형태의 데이터로 전달. javascript에서 html을 작성하기
function exchangeRate2(){
	$.ajax("${path}/ajax/exchange2",{
		success : function(json){
			//json : 서버에서 JSON 객체로 전달 받음.
			console.log(json)
			let html = "<table class='nopadding'>"
			html += "<caption>KEB하나은행<br>("+json.date+")</caption>"
			html += "<tr><th rowspan='2'>코드</th>"
			html += "<th rowspan='2'>기준율</th>"
			html += "<th colspan='2'>현찰</th></tr>"
			html += "<tr><th>파실때</th><th>사실때</th></tr>"
			//json.list : 통화 정보의 배열 객체
			$.each(json.list,function(i,obj){
				html += "<tr><td>"+obj[0]+" ("+obj[1]+")</td>"
				html +=	"<td>"+obj[2]+"</td>"
				html += "<td>"+obj[3]+"</td>"
				html += "<td>"+obj[4]+"</td></tr>"
			})
			$("#exchange2").html(html+"</table>");
		},
		error: function(e){
			alert("환율 조회시 서버 오류:"+e.status);
		}
	})
}

</script>

</body>
</html>