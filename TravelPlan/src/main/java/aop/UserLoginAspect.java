package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component //객체화 되는 클래스
@Aspect	//AOP 객체.
public class UserLoginAspect {
	/* -- PointCut : 핵심 메서드를 선택하는 기능. 
	 * .. : 갯수에 상관 없음. 매개변수가 없는경우도 포함
	 * execution(* controller.User*.loginCheck*(..))
	 *    controller 패키지의 User로 시작하는 클래스의 
	 *    매개변수 상관 없이 loginCheck로 시작하는 메서드가 실행될때.
	 * args(..,session)
	 *    메서드의 매개변수 목록 중 마지막 매개변수가 session인 메서드
	 *    
	 * && : controller 패키지의 User로 시작하는 클래스의 메서드 중 
	 *     메서드 명이 loginCheck로 시작하고 매개변수 목록의 마지막 변수가 session(HttpSession) 메서드가 실행될때. 
	 * 
	 * 조건에 맞는 메서드가 실행 될때 userLoginCheck 메서드를 먼저 실행 함.
	 * 
	 * -- Advice : pointcut 실행 시점 결정 
	 * @Around : 핵심 메서드 실행 전 후에 aop메서드가 실행됨.
	 * @Before : 핵심 메서드 실행 전에 aop 메서드가 실행됨. 
	 * @AfterReturning : 핵심 메서드 정상 종료 후에 aop 메서드가 실행됨. 
	 * @AfterThrowing : 핵심 메서드 예외 종료 후에 aop 메서드가 실행됨. 
	 * @After         : 핵심 메서드 종료 후에 aop 메서드가 실행됨. 
	 */
	
	@Around("execution(* controller.User*.loginCheck*(..)) && args(..,session)")
	public Object userLoginCheck(ProceedingJoinPoint joinPoint,HttpSession session)	throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser"); //로그인 정보 조회
		if(loginUser==null) {//로그아웃 상태
			throw new LoginException("[userLogin]로그인 후 거래하세요.", "login");
		}
		//joinPoint : 실행되는 메서드들을 관리하는 객체
		return joinPoint.proceed(); //다음 메서드(핵심메서드)를 호출.
	}
	//UserIdCheck 메서드
	@Around("execution(* controller.User*.idCheck*(..)) && args(id,session,..)")
	public Object userIdCheck(ProceedingJoinPoint joinPoint,String id, HttpSession session)	throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser"); //로그인 정보 조회
		if(loginUser==null) {//로그아웃 상태
			throw new LoginException("[idCheck]로그인 후 거래하세요.", "login");
		} else if (!loginUser.getUserid().equals(id) && !loginUser.getUserid().equals("admin")) {
			throw new LoginException("[idCheck]본인 정보만 거래 가능합니다.", "main");
		}
		return joinPoint.proceed();//다음 메서드(핵심메서드)를 호출
		}
	// 1.로그아웃 : 로그인하세요. login 페이지로 이동
	//2. 관리자 아닌 경우 : 관리자만 거래 가능합니다. main 페이지로 이동
	@Around("execution(* controller.Admin*.*(..)) && args(..,session)")
	public Object adminCheck(ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser==null) {
			throw new LoginException("[adminCheck]로그인 후 거래하세요.", "login");
		} else if (!loginUser.getUserid().equals("admin")) {	
			throw new LoginException("[adminCheck]관리자만 거래가능합니다.", "main");
		}
		return joinPoint.proceed();
		}

}

