package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.Sale;



public interface SaleMapper {
	
	@Select("select nvl(max(saleid),0) from sale")
	int maxsaleid();
	/*
	 * sale 객체의 프로처티 값 : #{saleid}, #{userid}
	 * sysdate : 오라클 예약어.현재일시
	 * 	 */
	
	@Insert("insert into sale (saleid, userid, saledate) "
			+ "values (#{saleid}, #{userid},sysdate)")
	void insert(Sale sale);
	
	@Select("select * from sale where userid=#{value} order by saleid desc")
	List<Sale> select(String id);
}
