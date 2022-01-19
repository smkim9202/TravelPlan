package controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.BoardException;
import logic.Board;
import logic.ShopService;

@Controller  //설정으로객체화(@Component) + 기능 (컨트롤러)  
@RequestMapping("board")  //요청정보 매핑. url : board  
public class BoardController {
	@Autowired //객체 주입. ShopService객체를 저장.  
	ShopService service;

	//http://localhost:8090/springstudy/board/write
	// @GetMapping : GET 방식 요청정보 매핑
	@GetMapping("write")
	public ModelAndView getBoard() {
		ModelAndView mav = new ModelAndView(); //기본 뷰는 url로 부터 설정됨.
		Board board = new Board();
		mav.addObject("board",board); //board 객체를 뷰로 전달.
		return mav;  //뷰의 이름과, 전달한 데이터를 리턴. 
	}

	
	//Post 방식 요청 처리
	@PostMapping("write")
 	                       //@Valid : 유효성 검사를함. 결과 bresult 객체 전달. 
	                       //board : 파라미터 이름과 프로퍼티이름과 비교하여 파라미터값을 저장 
                           //         board 객체에 파라미터값을 저장상태로 매개변수로 전달
	public ModelAndView write(@Valid Board board, BindingResult bresult,
			HttpServletRequest request ) {
		System.out.println(board);
		ModelAndView mav = new ModelAndView();
		//bresult 객체에 오류가 존재하는경우
		if(bresult.hasErrors()) {
			System.out.println(bresult.getModel());
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//bresult 객체에 오류가 없는 경우. 정확하게 입력 된경우.
		//업로드된 파일을 서버에 저장. db에 파라미터값을 저장.
		service.boardwrite(board,request);
	    // 뷰를 list로 재요청하기
		mav.setViewName("redirect:list"); //list 요청페이지를 브라우저가 재요청하도록 뷰선택.
		return mav;                       // 브라우저의 url 정보가 board/list 변경. 
	}
	@RequestMapping("list") //GET,POST 상관없이 board/list 요청시 호출
	public ModelAndView list(Integer pageNum, String searchtype, String searchcontent) { 
		ModelAndView mav = new ModelAndView();
		//pageNum 파라미터의 값이 없는 경우
		if(pageNum == null || pageNum.toString().equals("")) {
		   pageNum = 1;
		}
		if(searchtype == null || searchcontent == null ||
				searchtype.trim().equals("") || searchcontent.trim().equals("")) {
			searchtype = null;
			searchcontent = null;
		}
		int limit = 10; //한페이지에 보여질 게시물의 건수
		int listcount = service.boardcount(searchtype,searchcontent); //전체 게시물 등록 건수  
		List<Board> boardlist = service.boardlist(pageNum,limit,searchtype,searchcontent); //페이지에 출력한 게시물 목록 
        /*
         * 게시물건수   페이지수  
         *    10       1 :  10.0 / 10 + 0.95 => int(1.95) => 1 
         *    11       2 :  11.0 / 10 => 1.1 + 0.95 => int(2.05) => 2
         *   111       12:  111.0 / 10 => 11.1 + 0.95 => int(12.05) => 12 
         *   300       30:  300.0 / 10 => 30.0 + 0.95 => int(30.95) => 30 
         *   301       31:  301.0 / 10 => 30.1 + 0.95 => int(31.05) => 31 
         */
		int maxpage = (int)((double)listcount/limit + 0.95);//최대 필요한 페이지 수
		/*
		 *  화면에 표시될 페이지의 갯수 : 10개만  
		 *  화면에 표시될 시작 페이지 번호
		 *    현재페이지   시작페이지
		 *       2         1 : 2/10.0 => 0.2 + 0.9=>1.1 - 1  => int(0.1) * 10 + 1 => 1
		 *      10         1 : 10/10.0 => 1.0 + 0.9=>1.9 - 1  => int(0.9) * 10 + 1 => 1
		 *      20        11 : 20/10.0 => 2.0 + 0.9=>2.9 - 1  => int(1.9) * 10 + 1 => 11 
		 *      21        21 : 21/10.0 => 2.1 + 0.9=>3.0 - 1  => int(2.0) * 10 + 1 => 21
		 *      22        21 : 22/10.0 => 2.2 + 0.9=>3.1 - 1  => int(2.1) * 10 + 1 => 21
		 */
		int startpage = (int)((pageNum/10.0 + 0.9) - 1) * 10 + 1;//화면에 표시할 페이지의 시작 번호
		int endpage = startpage + 9;
		if(endpage > maxpage) endpage = maxpage; //화면에 표시할 페이지의 끝 번호
		int boardno = listcount - (pageNum - 1) * limit; //화면 표시될 게시물 번호. 의미없음
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage", maxpage);
		mav.addObject("startpage", startpage);
		mav.addObject("endpage", endpage);
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardno", boardno);
		return mav;
	}	
	@GetMapping("detail")  
	public ModelAndView detail(Integer num) {
		ModelAndView mav = new ModelAndView();
		if (num != null) {
		    Board board = service.getBoard(num);
		    service.readcntadd(num);  //조회된 레코드의 조회건수 1증가
		    mav.addObject("board", board);
		}    
		return mav;   // board/detail 뷰로 설정.
	}
	@GetMapping("*")  //Get방식 요청이 설정 되지 않은 경우 호출되는 메서드
	public ModelAndView getBoard(Integer num) {
		ModelAndView mav = new ModelAndView();
		if (num != null) {
		    Board board = service.getBoard(num);
		    mav.addObject("board", board);
		} else     mav.addObject(new Board());
		return mav;   // board/update 뷰로 설정.
	}
	/*
	 * 1. 파라미터 값 Board 객체 저장. 유효성 검증.
	 * 2. 입력된 비밀번호와 db의 비밀번호를 비교 
	 *    - 비밀번호가 맞는 경우
	 *      수정정보를 db에 변경
	 *      첨부파일 변경 : 첨부파일 업로드, fileurl 정보 수정
	 *      detail 페이지 호출 
	 *       
	 *    - 비밀번호가 틀린경우
	 *      예외발생하여  
	 *      '비밀번호가 틀립니다.', update Get방식으로 호출
	 */	
	@PostMapping("update")
	public ModelAndView update(@Valid Board board,
			BindingResult bresult,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		Board dbBoard =	service.getBoard(board.getNum());
		//board.getPass() : 화면에서 입력받은 비밀번호
		//dbBoard.getPass() : db에 등록된 비밀번호 
		if(!board.getPass().equals(dbBoard.getPass())) {
			//throw : 예외 강제 발생. 
			throw new BoardException("비밀번호가 틀립니다.",
					"update?num="+board.getNum());
		}
		try {
			board.setFileurl(request.getParameter("file2"));
			service.boardUpdate(board, request);
			mav.setViewName
			("redirect:detail?num="+board.getNum());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BoardException("게시글 수정을 실패 했습니다.",
					"update?num="+board.getNum());
		}
		return mav;
	}	
	/*
	 * 1. num, pass 파라미터 저장.
	 * 2. db의 비밀번호와 입력된 비밀번호가 틀리면 error.login.password
	 *    코드 입력.=> 유효성 검증 내용 출력하기
	 * 3. db에서 해당 게시물 삭제.
	 *    삭제 실패 : 게시글 삭제 실패. delete 페이지로 이동
	 *    삭제 성공 : list 페이지 이동    
	 */	
	@PostMapping("delete")
	public ModelAndView delete(Board board,BindingResult bresult){
		ModelAndView mav = new ModelAndView();
		int num =board.getNum();
		String pass = board.getPass();
		try {
			Board dbboard = service.getBoard(num);
			if(!pass.equals(dbboard.getPass())) {
				bresult.reject("error.login.password"); //오류 등록.
				return mav;
			}
			service.boardDelete(num);
			mav.setViewName("redirect:list");
		}catch (Exception e) {
			e.printStackTrace();
			throw new BoardException("게시물 삭제 실패", "delete?num="+num);
		}		
	    return mav;
    }
	/*
	   1. 파라미터 값을 Board 객체에 저장하기. 유효성 검증하기
	      원글정보 : num, grp, grplevel, grpstep  => hidden 정보
	      답글정보 : name, pass, subject, content => 등록정보
	   2. service.boardReply()
	      - 같은 grp 값을 사용하는 게시물들의 grpstep 값을 1 증가 하기.
	        boardDao.grpStepAdd(grp,grpstep)
	      - Board 객체를 db에 insert 하기.
	        num : maxnum + 1
	        grp : 원글과 동일.
	        grplevel : 원글 + 1
	        grpstep : 원글 + 1
	   4. 등록 성공시 :" list로 페이지 이동
	      등록 실패시 :" 답변등록시 오류발생"메시지 출력 후, reply로 페이지 이동하기 
	 */
	@PostMapping("reply")
	public ModelAndView reply (@Valid Board board,BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			Board dbBoard = service.getBoard(board.getNum());
			// bresult.getModel() : 오류 정보를 저장하고 있는 객체
			Map<String, Object> map = bresult.getModel();
			Board b = (Board)map.get("board"); //화면에서 입력된 값 저장
			b.setSubject(dbBoard.getSubject());
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			service.boardReply(board);
			mav.setViewName("redirect:list");
		} catch (Exception e) {
			e.printStackTrace();
	        throw new BoardException
           ("답글 등록에 실패 했습니다.","reply?num="+board.getNum());
		}
		return mav;
	}
	/*
	 * upload : CKEditor에서 선택된 이미지 파일의 이름 
	 *         <input type="file" name="upload" >
	 * CKEditorFuncNum : ckeditor에서 업로드 할 이미지별로 고유의 값을 전달.  
	 * Model : 뷰에 전달할 데이터를 저장하는 객체        
	 */
	@RequestMapping("imgupload")
	public String imgupload(MultipartFile upload, String CKEditorFuncNum,
			HttpServletRequest request,Model model ) {
		//업로드될 서버의 폴더 위치의 절대 경로.
		String path = request.getServletContext().getRealPath("/") + "board/imgfile/";
		File f =new File(path);
		//f.exists() : 파일(폴더) 존재? 
		//f.mkdirs(); 폴더 생성. 
		if(!f.exists()) f.mkdirs();
		
		if(!upload.isEmpty()) { 
			//업로드 될 파일의 절대경로로 File 객체 설정
			File file = new File(path,upload.getOriginalFilename());
			try {
				upload.transferTo(file); //파일 업로드. 서버에 파일로 저장 
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		//request.getContextPath() : /springstudy 프로젝트명. 웹어플리케이션이름
		//fileName : 업로드된 파일의 웹어플리케이션 경로 설정
		//            /springstudy/board/imgfile/apple.jpg
		String fileName = request.getContextPath() + "/board/imgfile/" 
		               + upload.getOriginalFilename();
		model.addAttribute("fileName",fileName);
		model.addAttribute("CKEditorFuncNum",CKEditorFuncNum);
		return "ckedit";
	}
	
}
