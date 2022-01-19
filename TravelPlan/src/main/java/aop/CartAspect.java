package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import exception.CartException;
import exception.LoginException;
import logic.Cart;
import logic.User;

@Component //객체화 되는 클래스
@Aspect	//AOP 객체.
public class CartAspect {
	@Around("execution(* controller.Cart*.check*(..)) && args(..,session)")
	public Object cartCheck(ProceedingJoinPoint joinPoint,HttpSession session)	throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser"); //로그인 정보 조회
		if(loginUser==null) {//로그아웃 상태
			throw new LoginException("회원만 주문 가능합니다. 로그인 후 거래하세요.", "../user/login");
		}
		//joinPoint : 실행되는 메서드들을 관리하는 객체
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size() == 0)
			throw new CartException("장바구니에 주물할 상품이 없습니다.","../item/list");
		
		return joinPoint.proceed(); //다음 메서드(핵심메서드)를 호출.
	}


}
