package com.bluedon.service;

import java.util.List;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.TbItem;

public interface CartService {

	/**
	 * @param tbItem 商品信息 (因为如果传入商品id，就要调用别的服务根据id查询商品，不方便，所以传入pojo对象)
	 * @param userId 用户id
	 * @param num 商品数量
	 * @return
	 */
	public TaotaoResult addCatItem(TbItem tbItem,String userId,Integer num);
	
	/**根据用户id查询商品列表
	 * @param userId
	 * @return
	 */
	public List<TbItem> getCartList(Long userId);
	
	/**根据用户id和商品id更新商品数量
	 * @param userId
	 * @param itemId
	 * @param num 新的商品数量
	 * @return
	 */
	public TaotaoResult updateCatByItemId(Long userId,Long itemId,Integer num);
	
	/**根据用户id和商品id删除购物车数据
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public TaotaoResult deleteCatByUserIdItemId(Long userId,Long itemId);
	
}
