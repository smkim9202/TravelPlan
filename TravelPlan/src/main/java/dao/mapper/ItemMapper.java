package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.Item;
/*
 *  CRUD
 *  Create : insert 구문
 *  Read : select 구문
 *  Update : update 구문
 *  Delete : delete 구문
 */
public interface ItemMapper {
	
	@Select({"<script>",
		 "select * from item ",
		 "<if test='id != null'> where id=#{id}</if>",
		 "order by id",
		 "</script>"})	
	List<Item> select(Map<String,Object> param);

	@Select("select nvl(max(id),0) from item")
	int maxNo();

	@Insert("insert into item (id,name,price,description,pictureUrl )"
			+ " values (#{id},#{name},#{price},#{description},#{pictureUrl})")
	void insert(Item item);
	
	@Update("update item set name=#{name},price=#{price},description=#{description},"
			+ " pictureUrl=#{pictureUrl} where id=#{id}")
	void update(Item item);
	
	@Delete("delete from item where id=#{value}")
	void delete(String id);
}
