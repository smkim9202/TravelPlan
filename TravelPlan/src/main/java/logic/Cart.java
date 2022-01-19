package logic;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	private List<ItemSet> itemSetList = new ArrayList<ItemSet>();
	
	public List<ItemSet> getItemSetList() {
		return itemSetList;
	}
	
	public void push(ItemSet itemSet) {
    //itemSet : 장바구니에 추가할 상품 정보		
		int count = itemSet.getQuantity();
	//itemSetList : 이미 장바구니에 추가되어 있는 상품 목록	
		for(ItemSet old : itemSetList) {
			if(itemSet.getItem().getId() == old.getItem().getId()) {
				count = old.getQuantity() + itemSet.getQuantity();
				old.setQuantity(count);
				return;
			}
		}
		itemSetList.add(itemSet);
	}
	public long getTotal() { //get 프로퍼티 
		long sum = 0;
		for(ItemSet is : itemSetList) {
			sum += is.getItem().getPrice() * is.getQuantity();
		}
		return sum;
	}
}
