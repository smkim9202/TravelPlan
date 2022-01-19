package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.ItemMapper;
import logic.Item;

@Repository
public class ItemDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<String,Object>();
	
	public List<Item> list() {
		return template.getMapper(ItemMapper.class).select(null);
	}
	public Item selectOne(Integer id) {
		param.clear();
		param.put("id",id);
		return template.getMapper(ItemMapper.class).select(param).get(0);
	}
	public int maxNo() {
		return template.getMapper(ItemMapper.class).maxNo();
	}
	public void insert(Item item) {
		template.getMapper(ItemMapper.class).insert(item);
	}
	public void update(Item item) { 
		template.getMapper(ItemMapper.class).update(item);
	}
	public void delete(String id) {
		template.getMapper(ItemMapper.class).delete(id);
	}
}
