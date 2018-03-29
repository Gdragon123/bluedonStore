package com.bluedon.search.service;

import com.bluedon.common.pojo.SearchResult;
import com.bluedon.common.pojo.TaotaoResult;

public interface SearchItemsService {

	/**
	 * 导入商品类目到索引库
	 * @return
	 * @throws Exception 
	 */
	public TaotaoResult importAllItems() throws Exception;
	
	/**
	 * 获取查询结果
	 * @param query
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception 
	 */
	public SearchResult searchItem(String query , Integer page , Integer rows) throws Exception;
	
	/**
	 * 根据商品id更新索引库
	 * @param itemId
	 * @return
	 * @throws Exception 
	 */
	public TaotaoResult updateSearchItemById(long itemId) throws Exception;
	
}
