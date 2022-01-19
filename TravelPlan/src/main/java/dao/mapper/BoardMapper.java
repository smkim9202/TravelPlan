package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.Board;

public interface BoardMapper {

	@Insert("insert into board "
			+" (num,name,pass,subject,content,file1,regdate,readcnt,grp,grplevel,grpstep)"
			+" values (#{num},#{name},#{pass},#{subject},#{content},"
			+ "#{fileurl,jdbcType=VARCHAR},sysdate,"
			+" 0,#{grp},#{grplevel},#{grpstep})")
	void insert(Board board);

	@Select("select nvl(max(num),0) from board")
	int maxnum();

	@Select({"<script>",
		"select count(*) from board",
		"<if test='searchtype != null and searchcontent != null'>"
		+ " where ${searchtype} like '%${searchcontent}%'</if>",
		"</script>"})
	int count(@Param("searchtype") String searchtype,@Param("searchcontent") String searchcontent);
//&lt; : <
	@Select({"<script>",
		    "select * from "
			+ "(select rownum rnum , num, name,subject, content,file1 fileurl,regdate,"
			+ " grp,grplevel,grpstep, pass, readcnt from "
			+ "(select * from board "
			+ "<if test='searchtype != null and searchcontent != null'>"
			+ " where ${searchtype} like '%${searchcontent}%'</if>"
			+ " order by grp desc, grpstep asc))"
			+ " where rnum >= #{startrow} and rnum &lt;= #{endrow}",
			"</script>"})
	List<Board> list(Map<String, Object> param);

	@Select("select num, name,subject, content,file1 fileurl,regdate,"
			+ " grp,grplevel,grpstep, pass, readcnt from board where num=#{value}")
	Board selectOne(Integer num);

	@Update("update board set readcnt = readcnt+1 where num = #{value}")
	void readcntadd(Integer num);

	@Update("update board set name=#{name}, subject=#{subject}, content=#{content},"
			+ "file1=#{fileurl} where num=#{num}")
	void update(Board board);

	@Delete("delete from board where num=#{value}")
	void delete(int num);

	@Update("update board set grpstep = grpstep+1 "
			+ "where grp = #{grp} and grpstep > #{grpstep}")
	void updateGrpStep(Board board);
	
	/*
	 * name,cnt
	 * 홍길동  5
	 * 김삿갓  2

	 * => resultType : Map<String, Object> 형태로 설정
	 *    키    객체   키  객체
	 *   name=홍길동, cnt=5
	 *   name=김삿갓, cnt=2   
	 */
	@Select("select * from "
	+ " (select name , count(*) cnt from board group by name order by cnt desc) a"
	+ " where rownum <= 7")
	List<Map<String, Object>> graph1();
	/*
	 * {regdate=21-12-03,cnt=3}
	 * {regdate=21-12-02,cnt=5}
	 * ..
	 */
	@Select("select * from "
			+ "(select to_char(regdate,'yy-mm-dd') regdate,count(*) cnt from board "
			+ " group by to_char(regdate,'yy-mm-dd') order by regdate desc ) a"
			+ " where rownum <= 7")
	List<Map<String, Object>> graph2();	
}

