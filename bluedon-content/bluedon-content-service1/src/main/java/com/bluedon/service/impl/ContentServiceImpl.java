package com.bluedon.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluedon.common.pojo.EasyUIDataGridResult;
import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.mapper.TbContentMapper;
import com.bluedon.pojo.TbContent;
import com.bluedon.pojo.TbContentExample;
import com.bluedon.redis.JedisClient;
import com.bluedon.service.ContentService;
import com.bluedon.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient client;
	
	@Value("${CONTENTKEY}")
	private String CONTENTCATID;
	
	@Override
	public TaotaoResult saveContent(TbContent tbContent) {
		//1.注入mapper
		//2.补全其他属性
		tbContent.setCreated(new Date());
		tbContent.setUpdated(tbContent.getCreated());
		//3.调用方法
		contentMapper.insert(tbContent);
		
		try {
			//4.同步缓存 删除key
			client.hdel(CONTENTCATID, tbContent.getCategoryId()+"");
			System.out.println("同步缓存成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//4.返回结果
		return TaotaoResult.ok();
	}

	@Override
	public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
		//1.注入mapper
		//2.开始分页
		PageHelper.startPage(page, rows);
		//3.设置分页条件
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		//4.根据条件查询数据
		List<TbContent> list = contentMapper.selectByExample(example);
		//5.获取分页数据
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		//6.封装成EasyUIDataGridResult
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) pageInfo.getTotal());
		result.setRows(pageInfo.getList());
		//7.返回
		return result;
	}

	@Override
	public List<TbContent> getContentCatListById(long categoryId) {
		try {
			//1.获取缓存中的数据
			String strJson = client.hget(CONTENTCATID, categoryId+"");
			//2.判断是否存在
			if(StringUtils.isNotBlank(strJson)){
				System.out.println("有缓存");
				//3.存在，直接返回
				return JsonUtils.jsonToList(strJson, TbContent.class);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//4.不存在，去数据库查询
		//1.注入mapper
		//2.设置查询条件
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		//3.查询
		List<TbContent> list = contentMapper.selectByExample(example);
		try {
			//4.加入到缓存中
			//注入jedisClient
			//调用方法
			client.hset(CONTENTCATID, categoryId+"", JsonUtils.objectToJson(list));
			System.out.println("没缓存");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//5.返回
		return list;
	}

}
