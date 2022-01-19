package dao.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.User;

public interface UserMapper {
	
	@Insert("insert into useraccount (userid, password, username, phoneno, postcode,"
		  + " address, email, birthday)"
  	      + " values (#{userid},#{password},#{username},#{phoneno},#{postcode},"
	      + " #{address},#{email},nvl(#{birthday, jdbcType=DATE},''))" )
	void insert(User user);

	@Select({"<script>",
		"select * from useraccount ",
		"<if test='userid != null'> where userid = #{userid} </if>",
		"<if test='userids != null'> where userid in "
		+ "<foreach collection='userids' item='id' separator=',' open='(' close=')'>#{id}"
		+" </foreach></if>",
		" order by userid ",
   	    "</script>"})
	List<User> select(Map<String, Object> param);

	@Select({"<script>",
		"select ${col} from useraccount ",
		"<trim prefix='where' prefixOverrides='AND || OR'>"
		+"<if test='userid != null'> and userid=#{userid}</if>"
		+" and email=#{email} and phoneno=#{phoneno}</trim>",		
		"</script>"})
	String search(Map<String, Object> param);

	@Update("update useraccount set username=#{username}, birthday=#{birthday},"
			+ " phoneno=#{phoneno}, postcode=#{postcode}, address=#{address}, email=#{email} "
			+ " where userid=#{userid}")
	void update(User user);

	@Update("update useraccount set password= #{password} where userid=#{userid}")
	void passwordupdate(Map<String, Object> param);
	
	@Delete("delete from useraccount where userid=#{value}")
	void delete(String userid);

	@Select("select * from useraccount where userid=#{value}")
	User selectOne(String userid);
	
}
