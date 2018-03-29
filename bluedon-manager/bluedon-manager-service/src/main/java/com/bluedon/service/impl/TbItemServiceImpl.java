package com.bluedon.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.bluedon.common.pojo.EasyUIDataGridResult;
import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.mapper.TbItemCatMapper;
import com.bluedon.mapper.TbItemDescMapper;
import com.bluedon.mapper.TbItemMapper;
import com.bluedon.pojo.TbItem;
import com.bluedon.pojo.TbItemDesc;
import com.bluedon.pojo.TbItemExample;
import com.bluedon.redis.JedisClient;
import com.bluedon.service.TbItemService;
import com.bluedon.utils.IDUtils;
import com.bluedon.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class TbItemServiceImpl implements TbItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Resource(name="topicDestination")
	private Destination destination;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ITEM_INFO_KEY}")
	private String ITEM_INFO_KEY;
	
	@Value("${ITEM_INFO_KEY_EXPIRE}")
	private int ITEM_INFO_KEY_EXPIRE;
	
	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//1.注入mapper
		//2.设置分页
		if(page == null) page = 1;
		if(rows == null) rows = 30;
		PageHelper.startPage(page, rows);
		
		//3.设置查询条件
		TbItemExample example = new TbItemExample();

		//4.调用mapper查询
		List<TbItem> list = itemMapper.selectByExample(example);
		
		//5.获取分页信息
		PageInfo<TbItem> info = new PageInfo<>(list);
		
		//6.获取结果
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) info.getTotal());
		result.setRows(info.getList());
		
		return result;
		
	}

	@Override
	public  TaotaoResult saveItem(TbItem item, String itemDesc) {
		//1.注入mapper
		//2.封装商品信息
		final long itemId = IDUtils.genItemId();
		item.setId(itemId);
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		
		//3.封装商品详情
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemDesc(itemDesc);
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setCreated(item.getCreated());
		tbItemDesc.setUpdated(item.getUpdated());
		
		//4.插入商品基本信息
		itemMapper.insert(item);
		
		//5.插入商品描述信息
		itemDescMapper.insert(tbItemDesc);
		
		//发送消息到mq
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId+"");
			}
		});
		
		//6.返回结果
		return TaotaoResult.ok();
		
	}

	@Override
	public TbItem getTbItemById(long itemId) {
		//添加缓存，需要捕捉异常，不影响正常业务逻辑
		try {
			if(itemId+"" != null){
				//从缓存中获取数据
				String str = jedisClient.get(ITEM_INFO_KEY+":"+itemId+":BASE");
				if(StringUtils.isNotBlank(str)){
					//如果缓存中有数据，直接返回
					TbItem item = JsonUtils.jsonToPojo(str, TbItem.class);
					//更新有效期
					jedisClient.expire(ITEM_INFO_KEY+":"+itemId+":BASE", ITEM_INFO_KEY_EXPIRE);
					System.out.println("有缓存。。。"+item);
					return item;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//如果缓存中没有数据，去数据库中查询
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		
		try {
			if(itemId+"" != null){
				System.out.println("没缓存。。。");
				//存入缓存中，并设置有效期
				String strJson = JsonUtils.objectToJson(tbItem);
				jedisClient.set(ITEM_INFO_KEY+":"+itemId+":BASE",strJson);
				jedisClient.expire(ITEM_INFO_KEY+":"+itemId+":BASE", ITEM_INFO_KEY_EXPIRE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItem;
	}

	@Override
	public TbItemDesc getTbItemDescById(long itemId) {
		try {
			if(itemId+"" != null){
				//从缓存中获取数据
				String descJson = jedisClient.get(ITEM_INFO_KEY+":"+itemId+":DESC");
				//如果缓存中有数据，重新设置有效期，返回
				if(descJson != null){
					TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(descJson, TbItemDesc.class);
					jedisClient.expire(ITEM_INFO_KEY+":"+itemId+":DESC", ITEM_INFO_KEY_EXPIRE);
					System.out.println("有缓存----"+tbItemDesc);
					return tbItemDesc;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//如果缓存中没有数据，去数据库中查询
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		
		//将数据写入缓存，并设置有效期
		try {
			if(itemId+"" != null){
				System.out.println("没缓存-----");
				String strDesc = JsonUtils.objectToJson(itemDesc);
				jedisClient.set(ITEM_INFO_KEY+":"+itemId+":DESC", strDesc);
				jedisClient.expire(ITEM_INFO_KEY+":"+itemId+":DESC", ITEM_INFO_KEY_EXPIRE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
