package controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.ItemException;
import logic.Item;
import logic.ShopService;

@Controller
@RequestMapping("item")
public class ItemController {
	@Autowired
	private ShopService service;

	@RequestMapping("list")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		List<Item> itemlist = service.itemList();
		mav.addObject("itemList", itemlist);
		return mav;
	}

	@RequestMapping({ "detail", "edit", "confirm" })
	public ModelAndView detail(Integer id) {
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item", item);
		return mav;
	}

	@RequestMapping("create")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("item/add");
		mav.addObject(new Item());
		return mav;
	}

	@RequestMapping("register")
	public ModelAndView add(@Valid Item item, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/add"); // 입력값 오류 발생시 화면
		if (bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		// item 테이블에 insert, picture 업로드파일을 파일로 생성
		service.itemCreate(item, request);
		mav.setViewName("redirect:list");
		return mav;
	}

	/*
	 * 1. 수정 입력값을 유효성 검증 2. db에 내용 등록하기 3. 등록 후 list로 페이지 이동
	 */
	@RequestMapping("update")
	public ModelAndView update(@Valid Item item, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/edit"); // 입력값 오류 발생시 화면
		if (bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		System.out.println(item);
		service.itemUpdate(item, request);
		mav.setViewName("redirect:list");
		return mav;
	}

	// 상품 삭제하기
	// 삭제 완료 후 list로 페이지 이동하기
	// http://localhost:8090/springstudy/item/delete?id=1
	@RequestMapping("delete")
//	public String delete(Item item) { // => item.setId(request.getParameter("id")); 
	public String delete(String id) { // => request.getParameter("id");
		try {
			service.itemDelete(id);
		} catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new ItemException("주문된 상품입니다. 주문상품을 삭제 후 삭제가 가능합니다.","list");
			
		}
		  return "redirect:list"; 
		
		
	}
}
