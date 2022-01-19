package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import dao.mapper.UserMapper;
import logic.User;

@Repository
public class UserDao {
	@Autowired
	private SqlSessionTemplate template;  //SqlSessionTemplate 객체 주입 DI
	private Map<String,Object> param = new HashMap<String,Object>();
//dao.mapper.UserMapper 인터페이스 이용.
	public void insert(User user) {
		template.getMapper(UserMapper.class).insert(user);
	}
	public User selectOne(String userid) {
/*		
		param.clear();
		param.put("userid", userid);
		List<User> list = template.getMapper(UserMapper.class).select(param);
		if (list==null || list.isEmpty()) return null;
		else return	list.get(0);
*/	    
		return template.getMapper(UserMapper.class).selectOne(userid);	
	}
	
	public String search(User user, String url) {
		param.clear();
		param.put("email",user.getEmail());
		param.put("phoneno",user.getPhoneno());
		if(url.equals("id")) {        //idsearch 인경우
			param.put("col","substr(userid,1,length(userid)-2)||'**'");
		} else if(url.equals("pw")) { //pwsearch 인경우
			param.put("col","'**'||substr(password,3,length(password)-2)");
			param.put("userid", user.getUserid());
		}
		return template.getMapper(UserMapper.class).search(param);
	}
	public void update(User user) {
		template.getMapper(UserMapper.class).update(user);
	}
	public void passwordupdate(String userid, String chgpass) {
		param.clear();
		param.put("userid", userid);
		param.put("password", chgpass);
		template.getMapper(UserMapper.class).passwordupdate(param);
	}
	public void delete(String userid) {
		template.getMapper(UserMapper.class).delete(userid);
	}
	public List<User> list() {
		return template.getMapper(UserMapper.class).select(null);
	}
	public List<User> list(String[] idchks) {
		param.clear();
		param.put("userids", idchks);
		return template.getMapper(UserMapper.class).select(param);
	}
}

