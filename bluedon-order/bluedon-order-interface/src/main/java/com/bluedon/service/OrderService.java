package com.bluedon.service;

import java.util.List;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.OrderInfo;
import com.bluedon.pojo.TbItem;

public interface OrderService {

	/**根据用户id获取订单
	 * @param userId
	 * @return
	 */
	public List<TbItem> getOrderByUserId(long userId);
	
	/**生成订单
	 * @param orderInfo
	 * @return
	 */
	public TaotaoResult createOrder(OrderInfo orderInfo);
	
}
