package com.bluedon.service;

import com.bluedon.common.pojo.EasyUIDataGridResult;
import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.TbItem;
import com.bluedon.pojo.TbItemDesc;

public interface TbItemService {

	/**分页查询商品列表
	 * @param page	//当前页码
	 * @param rows	//每页显示行数
	 * @return
	 */
	public EasyUIDataGridResult getItemList(Integer page,Integer rows);
	
	/**新增商品 不包括商品详情
	 * @param item  //商品
	 * @param itemDesc  //商品详情
	 * @return
	 */
	public TaotaoResult saveItem(TbItem item,String itemDesc);
	
	/**根据id获取商品信息
	 * @param itemId
	 * @return
	 */
	public TbItem getTbItemById(long itemId);
	
	
	/**根据id获取商品详情
	 * @param itemId
	 * @return
	 */
	public TbItemDesc getTbItemDescById(long itemId);
	
}
