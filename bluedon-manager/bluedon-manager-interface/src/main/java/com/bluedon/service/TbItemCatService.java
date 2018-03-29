package com.bluedon.service;

import java.util.List;

import com.bluedon.common.pojo.EasyUITreeNode;

public interface TbItemCatService {
	
	/**根据父节点查询商品分类列表
	 * @param parentId
	 * @return
	 */
	public List<EasyUITreeNode> getItemCatList(Long parentId);
	
}
