package com.bluedon.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bluedon.common.pojo.SearchItem;
import com.bluedon.common.pojo.SearchResult;
import com.bluedon.search.dao.SearchItemDao;

@Repository
public class SearchItemDaoImpl implements SearchItemDao {

	@Autowired
	private SolrServer solrServer;
	
	@Override
	public SearchResult search(SolrQuery query) throws Exception {
		
		//1.创建连接solrserver
		//2.设置查询条件
		//3.执行查询
		QueryResponse response = solrServer.query(query);
		//4.获取结果集
		SolrDocumentList documentList = response.getResults();
		List<SearchItem> list = new ArrayList<>();
		SearchResult result = new SearchResult();
		result.setRecordCount(documentList.getNumFound());//设置总记录数
		//高亮
		Map<String,Map<String,List<String>>> highlighting = response.getHighlighting();
		
		//5.遍历结果集
		for (SolrDocument solrDocument : documentList) {
			SearchItem item = new SearchItem();
			item.setCategory_name((String) solrDocument.get("item_category_name"));
			item.setId(Long.parseLong((solrDocument.get("id").toString())));
			item.setImage((String) solrDocument.get("item_image"));
			item.setPrice((Long) solrDocument.get("item_price"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));
			//6.取高亮
			List<String> highList = highlighting.get(solrDocument.get("id")).get("item_title");
			String titleStr = "";
			if(highList != null && highList.size() > 0){
				//有高亮
				titleStr = highList.get(0);
			}else{
				//没有
				titleStr = solrDocument.get("item_title").toString();
			}
			item.setTitle(titleStr);
			list.add(item);
		}
		//7.返回
		result.setItemList(list);
		return result;
	}

}
