package com.bluedon.test;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSearchItem {

	//添加索引
	@Test
	public void testAddDocument() throws Exception{
		
		//1.创建连接 solrServer对象 httpsolrserver
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr");
		
		//2.创建文档对象 solrinputdocument 对象
		SolrInputDocument document = new SolrInputDocument();
		
		//3.向文档添加域
		document.addField("id", "test001");
		
		//4.添加文档到索引库
		solrServer.add(document);
		
		//5.提交commit
		solrServer.commit();
		
	}
	
	@Test
	public void testSearch() throws Exception{
		
		//1.创建连接
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr");
		
		//2.创建查询对象solrquery
		SolrQuery solrQuery = new SolrQuery("手机");
		
		//3.设置查询条件
		solrQuery.set("df", "item_title");
		
		//4.执行查询
		QueryResponse response = solrServer.query(solrQuery);
		
		//5.获取结果集
		SolrDocumentList documentList = response.getResults();
		System.out.println("查询命中的总记录数："+documentList.getNumFound());
		
		//6.遍历结果集
		for (SolrDocument solrDocument : documentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
		}
		
	}
	
}
