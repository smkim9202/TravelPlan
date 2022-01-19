package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ItemSet;
import logic.Sale;
import logic.Cart;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("cart")
public class CartController {
	@Autowired
	private ShopService service;

//http://localhost:8090/springstudy/cart/cartAdd?id=1&quantity=5
//Cart : session에 저장	
//같은 상품을 다시 추가 하는 경우 수량만 증가하도록 프로그램 수정하기
	@RequestMapping("cartAdd")
	public ModelAndView add(Integer id, Integer quantity,HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		//1. id에 해당하는 Item 데이터 저장
		Item item = service.getItem(id);
		//2. session에 저장된 Cart 객체를 조회
		Cart cart = (Cart)session.getAttribute("CART");
		if (cart == null) { //session에 "CART" 인 객체가 없다.
			cart = new Cart();
			session.setAttribute("CART",cart); //session에 등록 
		}
		// 파라미터 id 값에 해당하는 Item 정보, 수량을 ItemSet 객체로 저장 => Cart 객체에 추가 
		cart.push(new ItemSet(item,quantity));
		mav.addObject("message",item.getName()+":" + quantity + "개 장바구니 추가");
		mav.addObject("cart", cart);
		return mav;
	}
	@RequestMapping("cartDelete")
	public ModelAndView delete(int index, HttpSession session) {
/*
 * 매개변수 : int index => Object ArrayList.remove(index) => 순서에 해당하는 객체를 제거하고 제거된 객체를 리턴 		
 * 매개변수 : Integer index => void ArrayList.remove(index) => 객체를 제거 		
 */
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		ItemSet del =cart.getItemSetList().remove(index); 
		mav.addObject("message",del.getItem().getName()+"이(가) 장바구니에서 삭제됨" );
		mav.addObject("cart", cart);
		return mav;
	}
	//cartView 요청 구현하기. 
	@RequestMapping("cartView")
	public ModelAndView view(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		mav.addObject("cart", cart);
		return mav;
	}
	//checkout 요청
	@RequestMapping("checkout")
	public String checkout(HttpSession session) {
		return null;
	}
	/* 주문 확정 : end 요청
	 * 1. 로그인, 장바구니상품 검증 필요 => aop로 설정. 
	 * 2. 장바구니 상품을 saleitem 테이블에 저장하기
	 * 3. 로그인 정보로 주문 정보(sale)테이블에 저장.
	 * 4. 장바구니 상품 제거
	 * 5. 주문 정보 end.jsp 페이지로 출력
	 */
	@RequestMapping("end")
	public ModelAndView checkend(HttpSession session) {
		ModelAndView mav =new ModelAndView();
		Cart cart = (Cart)session.getAttribute("CART");
		User loginUser = (User)session.getAttribute("loginUser");
		Sale sale = service.checkend(loginUser,cart);
		mav.addObject("sale",sale);
		return mav;
	}
	
}
