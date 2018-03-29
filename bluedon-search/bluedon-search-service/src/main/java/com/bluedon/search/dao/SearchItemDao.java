package com.bluedon.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import com.bluedon.common.pojo.SearchResult;

public interface SearchItemDao {

	/**
	 * 根据条件搜索商品
	 * @param query
	 * @return
	 * @throws Exception 
	 */
	public SearchResult search(SolrQuery query) throws Exception;
	
}
