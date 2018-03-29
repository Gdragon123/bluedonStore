package com.bluedon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluedon.common.pojo.EasyUITreeNode;
import com.bluedon.mapper.TbItemCatMapper;
import com.bluedon.pojo.TbItemCat;
import com.bluedon.pojo.TbItemCatExample;
import com.bluedon.pojo.TbItemCatExample.Criteria;
import com.bluedon.service.TbItemCatService;

@Service
public class TbItemCatServiceImpl implements TbItemCatService {

	@Autowired
	private TbItemCatMapper mapper;
	
	@Override
	public List<EasyUITreeNode> getItemCatList(Long parentId) {
		//1.注入mapper
		//2.创建查询条件
		TbItemCatExample example = new TbItemCatExample();
		example.createCriteria().andParentIdEqualTo(parentId);
		
		//3.根据条件查询
		List<TbItemCat> itemCatList = mapper.selectByExample(example);
		
		//4.封装List<EasyUITreeNode>
		List<EasyUITreeNode> list = new ArrayList<>();
		for (TbItemCat itemCat : itemCatList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(itemCat.getId());
			node.setText(itemCat.getName());
			node.setState(itemCat.getIsParent()==true?"closed":"open");
			list.add(node);
		}
		
		return list;
	}

}
