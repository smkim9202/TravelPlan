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
    <a href="${path }" class="w3-bar-item w3-button w3-theme-l1">SpringStudy</a>
    <a href="${path }/item/list" class="w3-bar-item w3-button  w3-hover-white">item</a>
    <a href="${path }/board/list" class="w3-bar-item w3-button w3-hover-white">board</a>
 	<a href="${path }/chat/chat" class="w3-bar-item w3-button  w3-hover-white">chat</a>
 	<a href="${path }/user/main" class="w3-bar-item w3-button  w3-hover-white">usermain</a>
 	
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
  <h4 class="w3-bar-item"><b>Ajax</b></h4>
</div>
<div class="w3-container">
	<%--ajax을 통해 얻은 수출입은행 환율 정보 내용 출력 : 서버단에서 html --%>
	<div id="exchange"></div>
</div><br>
<div class="w3-container">
	<%--ajax을 통해 얻은 수출입은행 환율 정보 내용 출력 : JS로 html --%>
	<div id="exchange11"></div>
</div><br>
<div class="w3-container">
	<%--ajax을 통해 얻은 KEB하나은행 환율 정보 내용 출력 : 서버단에서 html --%>
	<div id="exchange2"></div>
</div><br>
<div class="w3-container">
	<%--ajax을 통해 얻은 KEB하나은행 환율 정보 내용 출력 : JS로 html --%>
	<div id="exchange21"></div>
</div><br>
</nav>

<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<!-- Main content: shift it to the right by 250 pixels when the sidebar is visible -->
<div class="w3-main" style="margin-left:440px; margin-right:20px;">

  <div class="w3-row w3-padding-64">
    <div class="w3-half">
      <div class="w3-container w3-padding-16">
       <div id="piecontainer" style="width:80%; border:1px solid #ffffff">
		<canvas id="canvas1" style="width:100%;"></canvas>
	   </div>
      </div>
    </div>
    <div class="w3-half">
      <div class="w3-container w3-padding-16">
       <div id="barcontainer"  style="width:80%; border:1px solid #ffffff">
		<canvas id="canvas2" style="width: 100%;"></canvas>
	   </div>
    </div> 
  </div>

	<div class="w3-row w3-padding-64">
		<decorator:body />
	</div>
	<div class="w3-row w3-padding-64">
		<div id="si" style="float:left; margin:20px;">
			<select name="si" onchange="getText('si')">
				<option value="">시도를 선택하세요</option>
			</select>		
		</div>
		<div id="gu" style="float:left; margin:20px;">
			<select name="gu" onchange="getText('gu')">
				<option value="">구군을 선택하세요</option>
			</select>
		</div>
		<div id="dong" style="float:left; margin:20px;">
			<select name="dong">
				<option value="">동리를 선택하세요</option>
			</select>
		</div>	
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

// 그래프
var randomColorFactor = function(){
	  return Math.round(Math.random() * 255);//0~255 임의의값
}
var randomColor = function(opa) { //임의의 색상 리턴.
	  return "rgba("+ randomColorFactor() + ","
			  + randomColorFactor() + ","
			  + randomColorFactor() + ","
			  + (opa || '.3') + ")";
}

// layout 화면에 환율 정보 데이터를 가져오기
$(function(){
	//서버에서 객체
	exchangeRate(); //수출입은행 환율 정보(서버에서html)
	exchangeRate11(); //수출입은행 환율 정보
	exchangeRate2(); //하나은행 환율 정보(서버에서html)
	exchangeRate21(); //하나은행 환율 정보
	doselect();
    piegraph(); //글쓴이별 등록게시글건수. 최대 7개만 파이그래프 출력
	bargraph(); //최근 7일간 작성된 게시글 건수를 막대그래프 출력
})

//수출입은행 환율정보 : 서버에서 html 문장 작성을하여 화면에 출력
function exchangeRate(){
	$.ajax("${path}/ajax/exchange",{
		success : function(data){
			console.log(data)
			$("#exchange").html(data);
		},
		error: function(e){
			alert("환율 조회시 서버 오류:"+e.status);
		}
	})
}

//수출입은행 환율정보 : javascript에서 html을 작성하기
function exchangeRate11() {
	$.ajax("${path}/ajax/exchange11",{
		  success : function(json) {
			  console.log(json)
			  let html = "<table class='nopadding'>"
			  html += "<caption>수출입은행<br>" + json.date + "</caption>"
			  html += "<tr><th>통화</th><th>기준율</th><th>받을때</th><th>파실때</th></tr>";
			  $.each(json.list,function(i,obj) {
				  html += "<tr><td>"+obj[0] +"<br>"+obj[1]+"</td>"					  
				  html += "<td>"+obj[2] + "</td>"	  
				  html += "<td>"+obj[3] + "</td>"	  
				  html += "<td>"+obj[4] + "</td></tr>"	  
			  })
			  $("#exchange11").html(html+"</table>");
		  },
		  error : function(e){
			  alert("환율 조회시 서버 오류:"+e.status);
		  }
	  })	
}

//하나은행 : 서버에서 html 문장 작성을하여 화면에 출력
function exchangeRate2(){
	$.ajax("${path}/ajax/exchange2",{
		success : function(data){
			//data : html 형태의 text 문자열
			console.log(data)
			$("#exchange2").html(data);
		},
		error: function(e){
			alert("환율 조회시 서버 오류:"+e.status);
		}
	})
}

//하나은행 : 서버에서 JSON형태의 데이터로 전달. javascript에서 html을 작성하기
function exchangeRate21(){
	$.ajax("${path}/ajax/exchange21",{
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
			$("#exchange21").html(html+"</table>");
		},
		error: function(e){
			alert("환율 조회시 서버 오류:"+e.status);
		}
	})
}
	
//시선택
function doselect(){
	$.ajax({
		url : "${path}/ajax/select",
		success : function(data){//시도 정보 배열 형태
			//i: 인덱스
			//item : 데이터 정보. 시도데이터
			console.log(data)
			$.each(data,function(i,item){
				$("select[name='si']").append(function(){
					return "<option>" + item + "</option>"
				})
			});
		}
	})
}
//name:si : 시도 선택 => 구군정보
//name:gu : 구군 선택 => 동정보
//name:dong : 동정보 선택 => 없음. 서버에 전송안함
function getText(name){
	let cityval = $("select[name='si']").val(); //선택된 시도정보 : 서울특별시
	let guval = $("select[name='gu']").val();  //선택된 구군정보 : 강남구 
	let disname;
	let toptext="구군을 선택하세요";
	let params="";
	if (name=="si"){ //시도선택 => 결과는 구군정보
		params = "si=" + cityval.trim(); //si=서울특별시
		disname = "gu";
	}else if(name=="gu"){ //구군서택 => 결과는 동정보
		params = "si=" +cityval.trim()+"&gu="+guval.trim();
		disname = "dong";
		toptext="동리를 선택하세요";
	}else{ //name=dong
		return; //서버요청 안함
	}
	
	$.ajax({
		url : "${path}/ajax/select",
		type : "POST",
		data : params,
		success : function(data) {
			//data : name=si 인경우 : 구군정보 배열저장
			//data : name=gu 인경우 : 동정보 배열저장
			var selhtml="<select name='"+ disname +"' onchange='getText(\""+disname+"\")'>";
			selhtml += "<option value=''>" + toptext + "</option>"
			$.each(data, function(i,item){
				selhtml += "<option>" + item + "</option>"
			});
			selhtml+="</select>";
			$("#"+disname).html(selhtml);
		}
	})
}

//그래프
function piegraph() {
	  $.ajax("${path}/ajax/graph",{
		  success : function(data) {
			  //{"names": ["홍길동","김삿갓"],  "cnts":[5,2]}
	          console.log(data)
			  pieGraphPrint(data);
		  },
		  error : function(e) {
			  alert("서버 오류:" + e.status);
		  }
	  })
}
function bargraph() {
	  $.ajax("${path}/ajax/graph2",{
		  success : function(data) {
			  barGraphPrint(data); //최근 7일간의 게시글 등록 건수 막대 선그래프로 표시
		  },
		  error : function(e) {
			  alert("서버 오류:" + e.status);
		  }
	  })
}
function pieGraphPrint(data) {
  //{"names": ["홍길동","김삿갓"],  "cnts":[5,2]}
	var names = data.names
	var datas = data.cnts
	var colors = []
	$.each(datas,function(index) {
		colors[index] = randomColor(0.5); //랜덤한 색상으로 설정 
	})
	var config = {
		  type : 'pie',
		  data : {
			  datasets : [{	  data : datas,	  backgroundColor:colors }],
			  labels : names
		  },
	      options : {
	    	responsive : true,
	    	legend : {position : 'top'},
	    	title : {
	    		display : true,
	    		text : '글쓴이 별 게시판 등록 건수',
	    		position : 'bottom'
	    	}
	      }
	}
	var ctx = document.getElementById("canvas1").getContext("2d");
	new Chart(ctx,config);
}
function barGraphPrint(data) {
    var colors = [];
    $.each(data.cnts,function(index) {
		colors[index] = randomColor(0.7);
	})
	var chartData = {
			labels: data.regdates,
			datasets: [{
				type: 'line',
				borderWidth: 2,
				borderColor:colors,
				label: '건수',
				fill: false,
				data: data.cnts,
			}, {
				type: 'bar',
				label: '건수',
				backgroundColor: colors,
				data: data.cnts
			}]
	      }
	    var config = {
				type: 'bar',
				data: chartData,
				options: {			
					responsive: true,
					title: {display: true,
						    text: '최근 7일 게시판 등록 건수',
						    position : 'bottom'
					},
					legend : {display : false },
					scales: {
						xAxes: [{ display : true,   stacked : true }],			
						yAxes: [{ display : true,   stacked : true }]			
				    }
				}
	    }
		var ctx = document.getElementById('canvas2').getContext('2d');
		new Chart(ctx,config);
}
</script>

</body>
</html>