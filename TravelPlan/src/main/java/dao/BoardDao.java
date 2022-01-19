package dao;
/*
 * JDBC 이용하여 db 연결
 * 1. 드라이버 연결 : Class.forName("드라이버 클래스 이름")
 * 2. 연결 객체    : DriverManager.getConnection("url","userid","password")
 * 3. Statement 객체  : conn.createStatement()
 * 4. sql 구문실행 : stat.executeUpdate()
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import dao.mapper.BoardMapper;
import logic.Board;
@Repository
public class BoardDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<String,Object>();

	public void write(Board board) {
		int num = maxNum() + 1;
		board.setNum(num); 
		board.setGrp(num); 
		System.out.println("boarddao: "+board);
		template.getMapper(BoardMapper.class).insert(board);
	}
	private int maxNum() {
		return template.getMapper(BoardMapper.class).maxnum();
	}
	public int count(String searchtype,String searchcontent) {
		return template.getMapper(BoardMapper.class).count(searchtype,searchcontent);
	}
	
	public List<Board> list(Integer pageNum, int limit,String searchtype,String searchcontent) {
		param.clear();
		int startrow = (pageNum - 1) * limit + 1;
		int endrow = startrow + limit - 1;
		param.put("startrow", startrow);
		param.put("endrow", endrow);
		if(searchtype != null && searchcontent != null) {
			param.put("searchtype", searchtype);
			param.put("searchcontent", searchcontent);
		}
		return template.getMapper(BoardMapper.class).list(param);
	}
	//num에 해당하는 게시물을 조회하여 Board 객체로 리턴 
	public Board selectOne(Integer num) {
		return template.getMapper(BoardMapper.class).selectOne(num);
	}
	public void readcntadd(Integer num) {  //조회건수 증가
		template.getMapper(BoardMapper.class).readcntadd(num);
	}
	
	public void update(Board board) {
		template.getMapper(BoardMapper.class).update(board);
	}
	public void delete(int num) {
		template.getMapper(BoardMapper.class).delete(num);
	}
	public void updateGrpStep(Board board) {
		template.getMapper(BoardMapper.class).updateGrpStep(board);
	}
	public void reply(Board board) {
		board.setNum(maxNum() + 1);
		board.setGrplevel(board.getGrplevel()+1);
		board.setGrpstep(board.getGrpstep()+1);
		template.getMapper(BoardMapper.class).insert(board);
	}
	public List<Map<String, Object>> graph1() {
		return template.getMapper(BoardMapper.class).graph1();
	}
	public List<Map<String, Object>> graph2() {
		return template.getMapper(BoardMapper.class).graph2();
	}
}