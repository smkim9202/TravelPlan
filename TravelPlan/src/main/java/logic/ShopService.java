package logic;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.BoardDao;
import dao.ItemDao;
import dao.SaleDao;
import dao.SaleItemDao;
import dao.UserDao;

@Service   //설정에의한 객체화 + 기능(서비스기능:컨트롤러와 모델의 중간역할). @Component로 대체 가능
public class ShopService {
	@Autowired
	BoardDao boardDao;
	@Autowired
	UserDao userDao;
	@Autowired
	ItemDao itemDao;
	@Autowired
	SaleDao saleDao;
	@Autowired
	SaleItemDao saleItemDao;
	
	public void boardwrite(Board board, HttpServletRequest request) {
		//업로드된  파일 서버에 저장
		if(board.getFile1() != null && !board.getFile1().isEmpty()) { //업로드된 파일이 존재하면
			uploadFileCreate(board.getFile1(),request,"board/file");               //파일로 저장
			board.setFileurl(board.getFile1().getOriginalFilename()); //업로드된 파일의 이름
		}
		//db에 내용 추가
		boardDao.write(board);
	}
	public int boardcount(String searchtype,String searchcontent) {
		return boardDao.count(searchtype,searchcontent);
	}
	public List<Board> boardlist(Integer pageNum, int limit,String searchtype,String searchcontent) {
		return boardDao.list(pageNum,limit,searchtype,searchcontent);
	}
	public Board getBoard(Integer num) {
		return boardDao.selectOne(num);
	}
	public void readcntadd(Integer num) {
		boardDao.readcntadd(num);
	}
	public void boardUpdate(Board board, HttpServletRequest request) {
		//업로드된  파일 서버에 저장
		if(board.getFile1() != null && !board.getFile1().isEmpty()) { //업로드된 파일이 존재하면
			uploadFileCreate(board.getFile1(),request,"board/file/");               //파일로 저장
			board.setFileurl(board.getFile1().getOriginalFilename()); //업로드된 파일의 이름
		}
		//db에 내용 수정
		boardDao.update(board);
	}
	public void boardDelete(int num) {
		boardDao.delete(num);
	}
	
	//1. 기존 답글의 grpstep 값을 +1 수정
	//2. 답글 정보 추가. insert 
	public void boardReply(Board board) {
		boardDao.updateGrpStep(board);
		boardDao.reply(board);
	}
	//
	// 여기부터는 회원가입 부분입니다.
	public void userInsert(User user) {
		userDao.insert(user);
	}
	public User userSelectOne(String userid) {
		return userDao.selectOne(userid);
	}
	public String getSearch(User user, String url) {
		return userDao.search(user,url);
	}
	public void userUpdate( User user) {
		userDao.update(user);
	}
	public void userPassword(String userid, String chgpass) {
		userDao.passwordupdate(userid,chgpass);
	}
	public void userDelete(String userid) {
		userDao.delete(userid);
	}
	public List<User> userList() {
		return userDao.list();
	}
	public List<User> userList(String[] idchks) {
		return userDao.list(idchks);
	}
	public List<Item> itemList() {
		return itemDao.list();
	}
	public Item getItem(Integer id) {
		return itemDao.selectOne(id);
	}
	public void itemCreate(Item item, HttpServletRequest request) {
		item.setPictureUrl("");
		if(item.getPicture() != null && !item.getPicture().isEmpty()) { //업로드된 파일이 존재하면
			uploadFileCreate(item.getPicture(),request,"img/");               //파일로 저장
			item.setPictureUrl(item.getPicture().getOriginalFilename()); //업로드된 파일의 이름
		}
		int maxid = itemDao.maxNo();
		item.setId(maxid+1);		
		itemDao.insert(item);
	}
	//1. 상품수정전 화면 출력.
	//2. uploadPictureCreate, uploadFileCreate 하나의 함수로 수정하기 
	//uploadPictureCreate 제거해도 uploadFileCreate 함수로 모든 파일 업로드 처리하기

	private void uploadFileCreate(MultipartFile file, HttpServletRequest request,String upath) {
		String orgFile = file.getOriginalFilename(); //업로드된 파일의 실제 파일 명.
		//파일 업로드 위치
		//request.getServletContext().getRealPath("/") : 실제 웹어플리케이션 서버의 폴더 위치
		String uploadPath = request.getServletContext().getRealPath("/") + upath;
		File fpath = new File(uploadPath); 
		//fpath : 파일 업로드 위치 정보를 저장하고 있는 File 클래스의 객체
		//fpath.exists() : 폴더 존재?. 존재하면:True, 없으면 False 
		if(!fpath.exists()) fpath.mkdirs(); //폴더 생성. 파일 업로드시 폴더가 없으면 오류발생.
		try {
			file.transferTo(new File(uploadPath + orgFile)); //서버에 업로드된 파일을 저장.
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			uploadFileCreate(item.getPicture(),request,"img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.update(item);
	}
	public void itemDelete(String id) {
		itemDao.delete(id);
	}
	/*
	 * sale,saleitem 테이블에 저장하기
	 * 1. sale 테이블의 saleid의 최대값 조회 : 최대값+1
	 * 2. sale 정보 저장 : userid,sysdate
	 * 3. Cart데이터에서 saleitem 데이터 추출. insert
	 * 4. saleitem 정보를 sale 데이터 저장.
	 * 5. sale 데이터 리턴
	 */
	public Sale checkend(User loginUser, Cart cart) {
		//1. sale 테이블의 saleid의 최대값 조회 : 최대값+1
		int maxid = saleDao.getMaxSaleId();
		// 2. sale 정보 저장 : userid,sysdate
		Sale sale = new Sale();
		sale.setSaleid(maxid + 1);
		sale.setUser(loginUser); //주문 고객 정보
		sale.setUserid(loginUser.getUserid());
		saleDao.insert(sale);
		//3. Cart데이터에서 saleitem 데이터 추출. insert
		int seq = 0;
		for(ItemSet iset : cart.getItemSetList()) {
			SaleItem saleItem = new SaleItem(sale.getSaleid(),++seq,iset);
			//4. saleitem 정보를 sale 데이터 저장.
			sale.getItemList().add(saleItem);
			saleItemDao.insert(saleItem);
		}
		//5. sale 데이터 리턴
		return sale;
	}
	public List<Sale> salelist(String id) {
		List<Sale> list = saleDao.list(id);
		for (Sale sa : list) {
			List<SaleItem> saleitemlist = saleItemDao.list(sa.getSaleid());
			for (SaleItem si : saleitemlist) {
				Item item = itemDao.selectOne(si.getItemid());
				si.setItem(item);
			}
			sa.setItemList(saleitemlist);
		}
		return list;
	}
	public Map<String, Object> graph1() {
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> names = new ArrayList<>();
		List<Integer> cnts = new ArrayList<>();
		for(Map<String,Object> m : boardDao.graph1()) {
			// m : name=김삿갓, cnt=5
			names.add((String)m.get("NAME")); // 홍길동,김삿갓
			cnts.add(((BigDecimal)m.get("CNT")).intValue()); //5,2
		}
		map.put("names", names);
		map.put("cnts", cnts);
		return map; // {"names": ["홍길동","김삿갓"],  "cnts":[5,2]}
	}	
	public Map<String, Object> graph2() {
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> regdates = new ArrayList<>();
		List<Integer> cnts = new ArrayList<>();
		for(Map<String,Object> m : boardDao.graph2()) {
			regdates.add((String)m.get("REGDATE"));
			cnts.add(((BigDecimal)m.get("CNT")).intValue());
		}
		map.put("regdates", regdates);
		map.put("cnts", cnts);
		return map;
	}
}

