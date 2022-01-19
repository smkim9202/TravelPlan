package dao;

import java.util.List;



import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.SaleMapper;
import logic.Sale;

@Repository
public class SaleDao {
	@Autowired
	private SqlSessionTemplate template;
	
	public int getMaxSaleId() {
		return template.getMapper(SaleMapper.class).maxsaleid();
	}
	public void insert(Sale sale) {
		template.getMapper(SaleMapper.class).insert(sale);
	}
	public List<Sale> list(String id) {
		return template.getMapper(SaleMapper.class).select(id);
	}
}
