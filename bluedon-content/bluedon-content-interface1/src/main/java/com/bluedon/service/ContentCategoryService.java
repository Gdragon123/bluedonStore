package com.bluedon.service;

import java.util.List;

import com.bluedon.common.pojo.EasyUITreeNode;
import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.TbContentCategory;

public interface ContentCategoryService {

	/**
	 * 根据parentId查询商品内容分类
	 * @param parentId
	 * @return
	 */
	public List<EasyUITreeNode> getContentCategory(Long parentId);
	
	/**
	 * 新增商品内容分类
	 * @param category
	 * @return
	 */
	public TaotaoResult saveContentCategory(TbContentCategory category);
	
}
