package controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import logic.ShopService;



//@RestController : View 없이 Controller에서 직접 데이터를 클라이언트로 전송 기능
//					spring 4 버전 이후에서 가능함.
//					@Controller + @ResponseBody
//@ResponseBody : View 없이 Controller에서 직접 데이터를 클라이언틀 전송 기능
//@Controller : 클라이언트의 요청 + 객체화
@RestController
@RequestMapping("ajax")
public class AjaxController {
	@Autowired //객체 주입. ShopService객체를 저장.  
	ShopService service;
	//하나은행데이터 서버에서 html 문장 작성을하여 화면에 출력
	@RequestMapping(value="exchange",produces="text/html; charset=UTF-8")
	public String exchange() {
		Map<String, List<String>> map = new HashMap<>();
		StringBuilder html =null;
		try {
			//1. keb하나은행에서 환율데이터 가져오기
			String kebhana =Jsoup.connect("http://fx.kebhana.com/FER1101M.web").get().text();
			//2.json형태의 문자열만 가져오기
			String strjson = kebhana.substring(kebhana.indexOf("{"));
			//3.JSON 문자 파싱을 위한 객체 생성
			JSONParser parser = new JSONParser();
			//4. parser를 이용하여 json 문자열 파싱하기.
			JSONObject json = (JSONObject)parser.parse(strjson.trim());
			//5. "리스트"의 내용을 JSOnArray로 가져오기
			JSONArray array = (JSONArray)json.get("리스트");

			for(int i=0; i<array.size();i++) {
				//미국, 일본, 유로, 중국 통화만 정보를 하나씩 조회하기
				JSONObject obj = (JSONObject)array.get(i);
				if (obj.get("통화명").toString().contains("미국") ||
					obj.get("통화명").toString().contains("일본") ||
					obj.get("통화명").toString().contains("유로") ||
					obj.get("통화명").toString().contains("중국")) {
					String str = obj.get("통화명").toString();
					String[] sarr = str.split(" ");
					String key = sarr[0]; //국가명
					String code = sarr[1]; //통화코드
					List<String> list = new ArrayList<String>();
					list.add(code);
					list.add(obj.get("매매기준율").toString());
					list.add(obj.get("현찰파실때").toString());
					list.add(obj.get("현찰사실때").toString());
					//{"국가명",["통화코드","매매기준율","현찰파실때","현찰사실때"]}
					map.put(key, list);
				}
			}
			html = new StringBuilder();
			html.append("<table class='nopadding'>");
			html.append
			("<caption>KEB하나은행<br>("+json.get("날짜").toString() +")</caption>");
			html.append("<tr><th rowspan='2'>코드</th>");
			html.append("<th rowspan='2'>기준율</th>");
			html.append("<th colspan='2'>현찰</th></tr>");
			html.append("<tr><th>파실때</th><th>사실때</th></tr>");
			for (Map.Entry<String, List<String>> m : map.entrySet()) {
				html.append
				("<tr><td class='w3-center'>" + m.getKey()
				+ " (" + m.getValue().get(0) + ")</td>");
				html.append("<td>"+m.getValue().get(1)+"</td><td>"
				+ m.getValue().get(2)+"</td><td>"
				+ m.getValue().get(3)+"</td></tr>");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return html.toString();//문자열리턴. 클라이언트로 직접 전송. View를 거치지 않음.
	}
	
	//하나은행데이터 서버에서 JSON형태의 데이터로 전달. javascript에서 html을 작성하기
	@RequestMapping("exchange2")
	public Map<String,Object> exchange2() {
		Map<String, Object> map = new HashMap<>();
		try {
			String kebhana =Jsoup.connect("http://fx.kebhana.com/FER1101M.web").get().text();
			String strjson = kebhana.substring(kebhana.indexOf("{"));
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject)parser.parse(strjson.trim());
			JSONArray array = (JSONArray)json.get("리스트");
			List <List<String>> arrayList = new ArrayList<>();
			map.put("date", json.get("날짜").toString());
			for(int i=0; i<array.size();i++) {
				JSONObject obj = (JSONObject)array.get(i);
				if (obj.get("통화명").toString().contains("홍콩") ||
					obj.get("통화명").toString().contains("대만") ||
					obj.get("통화명").toString().contains("태국") ||
					obj.get("통화명").toString().contains("베트남")) {
					String str = obj.get("통화명").toString();
					String[] sarr = str.split(" ");
					String nat = sarr[0]; //국가명
					String code = sarr[1]; //통화코드
					List<String> list = new ArrayList<String>();
					list.add(nat);
					list.add(code);
					list.add(obj.get("매매기준율").toString());
					list.add(obj.get("현찰파실때").toString());
					list.add(obj.get("현찰사실때").toString());
					//{"국가명",["통화코드","매매기준율","현찰파실때","현찰사실때"]}
					arrayList.add(list);
				}
			}
			map.put("list", arrayList); //[국가명, 통화코드, 매매기준율, 현찰파실때, 현찰사실때],[..],[..],[..]]
		} catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	@RequestMapping("select")
	public List<String> select(String si, String gu, HttpServletRequest request) throws UnsupportedEncodingException{
		BufferedReader fr=null;
		try {
//			fr = new BufferedReader(new FileReader
//						(request.getServletContext().getRealPath("/")+"sido.txt"));
			fr = new BufferedReader(new InputStreamReader(new FileInputStream(request.getServletContext().getRealPath("/")+"sido.txt"),"UTF8"));
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		//fr.lines() : 파일의 내용을 Stream 객체로 리턴
		//			한줄씩 Stream으로 전달
		//flatMap : "서울시 강남구 역삼동" 의 내용중 서울시 컬럼의 내용들만 데이터로 변경.
		//			한줄로 들어가 있는 정보의 구조를 바꾼다.
		List<String> list = null;
		if (si == null && gu == null)
			list = fr.lines().flatMap(s->{//파라미터값이 없는 경우. 초기값으로 시도 정보추출 기능
			/*
		 		s : 서울시 강남구 역삼동 형태
				"\\s+" : 정규화 표현. 한개이상의 공백
						\\s : 공백
						+ 	: 1개이상
			arr : {"서울시","강남구","역삼동"} => 배열 형태
			arr[0] : 시도 정보
			arr[1] : 구군시 정보
			arr[2] : 동리 정보
			distinct() : 중복없이 데이터 한개만
			collect(Collectors.toList()) : 시도 정보를 List 객체로 변환
			list : 파일에 존재하는 시도 정보를 한개씩ㅁ나 List로 저장함
			  */
				String[] arr = s.split("\\s+");
				List<String> data = new ArrayList<String>();//시군구 문자들을 배열 저장
				//arr.length>=3 : 시도 구군 동리 정보가 모두 존재하는 데이터
				if(arr.length>=3) data.add(arr[0].trim());//시도정보만 data에 추가
				return data.stream();}).distinct().collect(Collectors.toList());
		else if(gu == null) {//si가 선택된경우. si 파라미터값:서울특별시
			list = fr.lines().flatMap(s->{ 
				String[] arr = s.split("\\s+"); 
				List<String> data = new ArrayList<String>();
				 //arr배열의 요소갯수가 3개이상이고, 첫번째 요소가 si파라미터 값과 같고, 
				   //  arr요소의 첫번째, 두번째 데이터가 다른 값인 경우.
				if(arr.length>=3 && arr[0].equals(si) && !arr[0].equals(arr[1]))
				{data.add(arr[1].trim());}
				return data.stream();
			}).distinct().sorted().collect(Collectors.toList());
		} else { //si파라미터값과 gu파라미터값이 모두 있는 경우 => 동정보 결과로 저장
			list = fr.lines().flatMap(s->{
				String[] arr = s.split("\\s+");
				List<String> data = new ArrayList<String>();
				if(arr.length>=3 && arr[0].trim().equals(si) &&
					arr[1].trim().equals(gu) && !arr[1].equals(arr[2])) {
					//4개정보로 분리된 경우 : 3번째 4번째를 함께 출력 
					if(arr.length > 3) arr[2] += " " +arr[3];
					data.add(arr[2].trim());
				}
				return data.stream();
			}).distinct().sorted().collect(Collectors.toList());			
		}
		return list;	
	}	
	   @RequestMapping("graph")
	   public Map<String, Object> graph1() {
		   return service.graph1();
	   }
	   @RequestMapping("graph2")
	   public Map<String, Object> graph2() {
			return service.graph2();
	   }   
 
}
