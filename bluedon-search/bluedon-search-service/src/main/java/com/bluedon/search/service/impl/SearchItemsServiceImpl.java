package com.bluedon.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlProcessor.ResolutionMethod;
import org.springframework.stereotype.Service;

import com.bluedon.common.pojo.SearchItem;
import com.bluedon.common.pojo.SearchResult;
import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.search.dao.SearchItemDao;
import com.bluedon.search.mapper.SearchItemMapper;
import com.bluedon.search.service.SearchItemsService;

@Service
public class SearchItemsServiceImpl implements SearchItemsService {

	@Autowired
	private SearchItemMapper mapper;
	
	@Autowired
	private SolrServer solrserver;
	
	@Autowired
	private SearchItemDao searchItemDao;
	
	@Override
	public TaotaoResult importAllItems() throws Exception {
		
		//1.注入mapper
		//2.获取所有的商品数据列表
		List<SearchItem> list = mapper.getSearchItemList();
		
		//3.注入solrserver对象
		//4.遍历商品列表，添加属性到文档域
		for (SearchItem searchItem : list) {
			//5.获取document对象
			SolrInputDocument document = new SolrInputDocument();
			//为文档对象添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());
			solrserver.add(document);
		}
		//6.提交
		solrserver.commit();
		
		return TaotaoResult.ok();
		
	}

	@Override
	public SearchResult searchItem(String query, Integer page, Integer rows) throws Exception {

		//封装查询条件
		SolrQuery solrQuery = new SolrQuery();
		if(StringUtils.isNotBlank(query)){
			solrQuery.setQuery(query);
		}else{
			solrQuery.setQuery("*:*");
		}
		//设置分页
		if(page==null)page=1;
		if(rows==null)rows=60;
		solrQuery.setStart((page-1)*rows);
		solrQuery.setRows(rows);
		//设置默认搜索域
		solrQuery.set("df", "item_keywords");
		//设置高亮
		solrQuery.setHighlight(true);//开启高亮
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"red\">");
		solrQuery.setHighlightSimplePost("</em>");
		//调用dao的方法执行查询 获取结果
		SearchResult result = searchItemDao.search(solrQuery);
		//设置总页数
		long recordCount = result.getRecordCount();
		long pageCount = recordCount/rows;
		if(recordCount%rows>0){
			pageCount++;
		}
		result.setPageCount(pageCount);
		//设置返回
		return result;
	}

	@Override
	public TaotaoResult updateSearchItemById(long itemId) throws Exception {
		//获取商品信息
		SearchItem searchItem = mapper.getItemById(itemId);
		//创建文档对象
		SolrInputDocument document = new SolrInputDocument();
		//为文档对象添加域
		document.addField("id", searchItem.getId());
		document.addField("item_title", searchItem.getTitle());
		document.addField("item_sell_point", searchItem.getSell_point());
		document.addField("item_price", searchItem.getPrice());
		document.addField("item_image", searchItem.getImage());
		document.addField("item_category_name", searchItem.getCategory_name());
		document.addField("item_desc", searchItem.getItem_desc());
		//添加文档到索引库
		solrserver.add(document);
		//提交
		solrserver.commit();
		return TaotaoResult.ok();
	}

}
