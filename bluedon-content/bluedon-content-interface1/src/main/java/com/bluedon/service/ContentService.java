package com.bluedon.service;

import java.util.List;

import com.bluedon.common.pojo.EasyUIDataGridResult;
import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.TbContent;

public interface ContentService {

	/**
	 * 添加商品内容
	 * @param tbContent
	 * @return
	 */
	public TaotaoResult saveContent(TbContent tbContent);
	
	
	/**
	 * 分页查询商品内容列表
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	public EasyUIDataGridResult getContentList(long categoryId,int page,int rows);
	
	/**
	 * 根据商品分类id获取商品分类列表
	 * @param categoryId
	 * @return
	 */
	public List<TbContent> getContentCatListById(long categoryId);
	
}
