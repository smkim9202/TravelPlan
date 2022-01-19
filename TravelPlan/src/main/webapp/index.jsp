<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>TravelPlan</title>
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

*{margin:0px; padding:0px;}

/* .animation_canvas : 이미지, 텍스트 포함하고 있는 영역 */
.animation_canvas{
	overflow :hidden; position: relative;
	width : 600px; height:400px;
}
.slider_panel{width:3000px; position:relative;}
.slider_image{float:left; width:600px; height:400px;}
.slider_text_panel{position:absolute; top:100px; left:50px;}
.slider_text{position:absolute; width:250px; height:150px;}
.control_panel{
	position:absolute; top:380px; left:270px;
	height:13px; overflow:hidden;
}
.control_button{
	width:12px; height:46px; position:relative; float:left;
	cursor: pointer; background:url('img/point_button.png');
}
.control_button:hover{top:-16px;} /*회색원*/
.control_button.select{top:-31px;} /*파란원*/
</style>
<script type="text/javascript" src=
"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<body>

<h1>TrvelPlan</h1>

<div class="animation_canvas">
	<div class="slider_panel">
		<img src="img/Desert.jpg" class="slider_image" />
		<img src="img/Hydrangeas.jpg" class="slider_image" />
		<img src="img/Jellyfish.jpg" class="slider_image" />
		<img src="img/Koala.jpg" class="slider_image" />
		<img src="img/Lighthouse.jpg" class="slider_image" />
	</div>
	<div class="slider_text_panel">
		<div class="slider_text"><h1>사막이미지</h1><p>더운사막</p></div>
		<div class="slider_text"><h1>수국이미지</h1><p>물에서자라는 수생식물</p></div>
		<div class="slider_text"><h1>해파리이미지</h1><p>해파리는 독이 있다.</p></div>
		<div class="slider_text"><h1>코알라이미지</h1><p>코알라는 유칼리나무잎만 먹는다</p></div>
		<div class="slider_text"><h1>등대이미지</h1><p>등대이미지</p></div>
	</div>
</div>
<div class="control_panel">
	<div class="control_button"></div>
	<div class="control_button"></div>
	<div class="control_button"></div>
	<div class="control_button"></div>
	<div class="control_button"></div>
</div>

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

	</div>
	<div class="w3-row w3-padding-64">
		<div id="si" style="float:left; margin:20px;">
			<select name="si" onchange="getText('si')">
				<option value="">지역을 선택하세요</option>
			</select>		
		</div>
		<div id="gu" style="float:left; margin:20px;">
			<select name="gu" onchange="getText('gu')">
				<option value="">국가를 선택하세요</option>
			</select>
		</div>
		<div id="dong" style="float:left; margin:20px;">
			<select name="dong">
				<option value="">도시를 선택하세요</option>
			</select>
		</div>	
	</div>   
</div>


<script type="text/javascript" 
	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js"></script>
<script>
//슬라이드배너
$(function(){
	$(".control_button").each(function(index){
		$(this).attr("idx",index); //idx=0, ...각각의 태그에 속성 추가 등록
	}).click(function(){
		var index = $(this).attr("idx");
		//mouseSlider : index에 해당하는 이미지를 화면에 출력
		moveSlider(index); //index : 선택된 버튼의 속성값. 선태값
	})
	$(".slider_text").css("left",-300).each(function(index){ //text 값을 -300. 화면에 보이지 않음
		$(this).attr("idx",index); //idx=0,...각각의 태그에 속성 추가 등록
	});
	
	moveSlider(0); //0 index에 해당하는 이미지를 화면에 출력
	var idx = 0;
	var inc = 1;
	//idx : 1 2 3 4
	//inc : 1
setInterval(function(){
	if(idx >= 4) inc = -1;
	if(idx <= 0) inc = 1;
	idx += inc;
	moveSlider(idx); //idx 해당하는 이미지를 화면에 출력
},2000)
})
//index에 해당하는 이미지, 텍스트, 버튼
function moveSlider(index){
	var moveLeft = -(index * 600);
	//animate : 해당 이미지 태그를 이동
	$(".slider_panel").animate({left:moveLeft},'slow'); //이미지처리
	
	//하단의 버튼 처리
	//class="select" 속성 추가. 파란원부분이 화면에 표시됨
	$(".control_button[idx=" + index + "]").addClass("select");
	$(".control_button[idx!=" + index + "]").removeClass("select"); //class="select" 속성제거
	
	//show() : 보이기
	$(".slider_text[idx=" + index + "]").show().animate({
		left : 0
	},"slow")
	//hide() : 숨기기
	$(".slider_text[idx!="+index+"]").hide("slow",function(){
		$(this).css("left",-300);
	})
}
//그래프
var randomColorFactor = function(){
	  return Math.round(Math.random() * 255);//0~255 임의의값
}
var randomColor = function(opa) { //임의의 색상 리턴.
	  return "rgba("+ randomColorFactor() + ","
			  + randomColorFactor() + ","
			  + randomColorFactor() + ","
			  + (opa || '.3') + ")";
}
//layout 화면에 환율 정보 데이터를 가져오기
$(function(){
	//서버에서 객체
	doselect();
    piegraph(); //글쓴이별 등록게시글건수. 최대 7개만 파이그래프 출력
	bargraph(); //최근 7일간 작성된 게시글 건수를 막대그래프 출력
})
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
	let toptext="국가를 선택하세요";
	let params="";
	if (name=="si"){ //시도선택 => 결과는 구군정보
		params = "si=" + cityval.trim(); //si=서울특별시
		disname = "gu";
	}else if(name=="gu"){ //구군서택 => 결과는 동정보
		params = "si=" +cityval.trim()+"&gu="+guval.trim();
		disname = "dong";
		toptext="도시를 선택하세요";
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
