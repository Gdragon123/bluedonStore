package com.bluedon.search.mapper;

import java.util.List;

import com.bluedon.common.pojo.SearchItem;


public interface SearchItemMapper {

	/**
	 * 查询商品列表
	 * @return
	 */
	public List<SearchItem> getSearchItemList();
	
	/**
	 * 根据商品id获取商品详情
	 * @param itemId
	 * @return
	 */
	public SearchItem getItemById(long itemId);
	
}
