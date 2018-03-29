package com.bluedon.test;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrCloudTest {

	//增加
	@Test
	public void testAdd() throws Exception{
		
		//1.创建solrserver对象
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.133:2182,192.168.25.133:2183,192.168.25.133:2184");
		
		//2.指定搜索的collection
		solrServer.setDefaultCollection("collection2");
		
		//3.创建solrinputdocument
		SolrInputDocument document = new SolrInputDocument();
		
		//4.向文档中添加域
		document.addField("id", "test009");
		document.addField("item_title", "美女一个想要吗？");
		
		//5.添加文档至索引域
		solrServer.add(document);
		
		//6.提交
		solrServer.commit();
		
	}
	
}
