package com.bluedon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluedon.common.pojo.EasyUITreeNode;
import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.mapper.TbContentCategoryMapper;
import com.bluedon.pojo.TbContentCategory;
import com.bluedon.pojo.TbContentCategoryExample;
import com.bluedon.service.ContentCategoryService;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{

	@Autowired
	private TbContentCategoryMapper categoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCategory(Long parentId) {
		//1.注入mapper
		//2.设置查询条件
		TbContentCategoryExample categoryExample = new TbContentCategoryExample();
		categoryExample.createCriteria().andParentIdEqualTo(parentId);
		//3.查询
		List<TbContentCategory> list = categoryMapper.selectByExample(categoryExample);
		//4.封装结果
		List<EasyUITreeNode> nodes = new ArrayList<>();
		for (TbContentCategory category : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(category.getId());
			node.setState(category.getIsParent()==true?"closed":"open");
			node.setText(category.getName());
			nodes.add(node);
		}
		//5.返回
		return nodes;
	}

	@Override
	public TaotaoResult saveContentCategory(TbContentCategory category) {

		//1.注入mapper
		//2.补全其他属性
		category.setCreated(new Date());
		category.setIsParent(false);
		category.setSortOrder(1);
		category.setStatus(1); //1 正常 2 删除
		//3.判断是否新增的节点的父节点是否为叶子节点，如果是，需要更新成非叶子节点；如果不是，不需要更新
		TbContentCategory parent = categoryMapper.selectByPrimaryKey(category.getParentId());
		if(parent.getIsParent()==false){
			//是叶子节点，更新
			parent.setIsParent(true);
			categoryMapper.updateByPrimaryKey(parent);
		}
		//5.调用插入方法
		categoryMapper.insert(category);
		//6.返回taotaoResult 要包含TbContentCategory
		return TaotaoResult.ok(category);
	}

}
