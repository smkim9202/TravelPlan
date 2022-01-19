package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Mail;
import logic.ShopService;
import logic.User;
/*
 * AdminController의 모든 메서드들은 반드시 관리자로 로그인 해야한 실행되도록
 * AOP 설정해야함.
 * 1. 로그아웃 : 로그인하세요. login 페이지로 이동
 * 2. 관리자 아닌 경우 : 관리자만 거래 가능합니다. main 페이지로 이동
 */
@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list")
	public ModelAndView list(Integer sort,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<User> userlist = service.userList();
		if (sort == null) sort = 0;
		if (sort == 0)
		    Collections.sort(userlist,(u1,u2)-> u1.getUserid().compareTo(u2.getUserid()) );
		else if (sort == 1) 
			Collections.sort(userlist,(u1,u2)-> u1.getUsername().compareTo(u2.getUsername()) );
		
		mav.addObject("list",userlist);
		return mav;
	}
	@RequestMapping("mailForm")
	public ModelAndView mailform(String[] idchks,HttpSession session) {
		ModelAndView mav = new ModelAndView("admin/mail"); //view를 설정
		/*
		 * if(true && 문장1) => 문장1 실행
		 * if(false && 문장2) => 문장2 실행안함. 문장2의 결과와 상관없이 전체 명제 False
		 * if(true || 문장3) => 문장3 실행안함 . 문장3의 결과와 상관없이 전체 명제 True
		 * if(false || 문장4) => 문장4 실행
		 */
		if(idchks == null || idchks.length==0) {
			throw new LoginException("메일 전송 대상자를 선택하세요","list");
		}
		//idchks : 회원목록에서 선택된 userid 에 회원 정보 목록
		List<User> userlist = service.userList(idchks);
		mav.addObject("list",userlist);
		return mav;
	}
	@RequestMapping("mail") //GET,POST 방식 모두 가능
	public ModelAndView mail(Mail mail,HttpSession session) {
		ModelAndView mav = new ModelAndView("alert"); //뷰이름 
		mailSend(mail);
		mav.addObject("message","메일 전송이 완료 되었습니다.");
		mav.addObject("url","list");
		return mav;
	}
	private final class MyAuthenticator extends Authenticator { //내부클래스 
		private String id;
		private String pw;
		public MyAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(id, pw);
		}
	}
	private void mailSend(Mail mail) {
		//메일 전송시 사용할 인증 객체 
		MyAuthenticator auth = new MyAuthenticator (mail.getNaverid(),mail.getNaverpw());
		//Properties : Map 객체 : <키, 값> 쌍인 객체
		//             Hashtable 하위클래스. 
		//             <String,String>형태인 Map 객체
		Properties prop = new Properties();
		try {
			FileInputStream fis =new FileInputStream
		("C:/20211108/spring/workspace/springstudy/src/main/resources/mail.properties");
			prop.load(fis); //mail.properties 파일의 내용을 키=값 형태로 저장
			prop.put("mail.smtp.user",mail.getNaverid());
		} catch (IOException e) {
			e.printStackTrace();
		}
		//prop : 메일 전송을 위한 환경 부분 설정 
		//auth : 인증 객체. 
		//session : 메일 전송을 위한 연결 객체 
		Session session = Session.getInstance(prop,auth);
		//mimeMsg : 메일 객체
		MimeMessage mimeMsg = new MimeMessage(session);
		try {
			//보내는 메일 주소 
			mimeMsg.setFrom(new InternetAddress(mail.getNaverid()+"@naver.com"));
			
			List<InternetAddress> addrs = new ArrayList<InternetAddress>();
			String[] emails = mail.getRecipient().split(",");
			for(String email : emails) {
			try {
				//한글처리를 위해 인코딩 부분 설정
				 addrs.add(new InternetAddress(new String(email.getBytes("utf-8"),"8859_1")));
			} catch(UnsupportedEncodingException ue) {
				ue.printStackTrace();
			}
		}
		// arr : 수신 메일 주소 들 
		InternetAddress[] arr =  new InternetAddress[emails.length];
		for(int i=0;i<addrs.size();i++) {
				arr[i] = addrs.get(i);
		}
		mimeMsg.setSentDate(new Date()); //보낸일자
		mimeMsg.setRecipients(Message.RecipientType.TO,arr); //수신메일 주소 설정 
		mimeMsg.setSubject(mail.getTitle()); //메일 제목
		//MimeMultipart : 내용부분 
		MimeMultipart multipart = new MimeMultipart();
		MimeBodyPart message = new MimeBodyPart(); //내용,첨부파일 분리 부분 
		message.setContent(mail.getContents(),mail.getMtype()); //내용부분 설정
		multipart.addBodyPart(message);  //내용 추가.
		//첨부파일 
		for(MultipartFile mf : mail.getFile1()) {
			if((mf != null) && (!mf.isEmpty())) { //첨부파일 존재.
				multipart.addBodyPart(bodyPart(mf)); //첨부파일을 메일에 추가
			}
		}
		mimeMsg.setContent(multipart); //내일, 첨부파일 저장 
		Transport.send(mimeMsg); //메일 전송.
	} catch(MessagingException me) {
		me.printStackTrace();
	}
}
	private BodyPart bodyPart(MultipartFile mf) {
		MimeBodyPart body = new MimeBodyPart();
		String orgFile = mf.getOriginalFilename(); 
		String path = "c:/mailupload/";
		File f = new File(path);
		//폴더가 없으면 폴더 생성
		if(!f.exists()) f.mkdirs(); 
		File f1 =new File(path + orgFile); //업로드할 파일 
		try {
			mf.transferTo(f1); //파일을 저장 
			body.attachFile(f1); //메일에 첨부파일 추가
			//한글 처리 설정 
            body.setFileName(new String(orgFile.getBytes("UTF-8"),"8859_1"));
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return body;
	}	
}
