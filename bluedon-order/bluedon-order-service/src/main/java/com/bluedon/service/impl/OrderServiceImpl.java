package com.bluedon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.mapper.TbOrderItemMapper;
import com.bluedon.mapper.TbOrderMapper;
import com.bluedon.mapper.TbOrderShippingMapper;
import com.bluedon.pojo.OrderInfo;
import com.bluedon.pojo.TbItem;
import com.bluedon.pojo.TbOrderItem;
import com.bluedon.pojo.TbOrderShipping;
import com.bluedon.redis.JedisClient;
import com.bluedon.service.OrderService;
import com.bluedon.utils.JsonUtils;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private JedisClient jedisClient;

	@Value("${TT_CART_REDIS_PRE_KEY}")
	private String TT_CART_REDIS_PRE_KEY;
	
	@Value("${ORDER_GEN_KEY}")
	private String ORDER_GEN_KEY;
	@Value("${ORDER_ID_BEGIN}")
	private String ORDER_ID_BEGIN;
	@Value("${ORDER_ITEM_ID_GEN_KEY}")
	private String ORDER_ITEM_ID_GEN_KEY;
	
	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	@Override
	public List<TbItem> getOrderByUserId(long userId) {
		//获取redis中的购物车信息
		Map<String,String> map = jedisClient.hgetAll(TT_CART_REDIS_PRE_KEY+":"+userId);
		List<TbItem> list = new ArrayList<>();
		//判断map是否为空
		if(map != null){
			//不为空，遍历
			for (Map.Entry<String, String> entry : map.entrySet()) {
				//将数据转成pojo对象，添加到list中
				TbItem item = JsonUtils.jsonToPojo(entry.getValue(), TbItem.class);
				list.add(item);
			}
		}
		//返回
		return list;
	}

	@Override
	public TaotaoResult createOrder(OrderInfo orderInfo) {
		//接收表单的数据
		//生成订单id
		if(!jedisClient.exists(ORDER_GEN_KEY)){
			//如果不存在，设置初始值
			jedisClient.set(ORDER_GEN_KEY, ORDER_ID_BEGIN);
		}
		//如果存在，加一
		String orderId = jedisClient.incr(ORDER_GEN_KEY).toString();
		//补全其他属性
		orderInfo.setOrderId(orderId);
		orderInfo.setPostFee("0");
		//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		Date date = new Date();
		orderInfo.setCreateTime(date);
		orderInfo.setUpdateTime(orderInfo.getCreateTime());
		//向订单表中插入数据
		orderMapper.insert(orderInfo);
		//获取页面传递过来的订单明细数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		//遍历
		for (TbOrderItem tbOrderItem : orderItems) {
			//生成订单明细id
			Long orderItemId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY);
			//补全其他属性
			tbOrderItem.setId(orderItemId+"");
			tbOrderItem.setOrderId(orderId);
			//向订单明细表插入数据
			orderItemMapper.insert(tbOrderItem);
		}
		//获取页面传递过来的订单物流数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		//补全其他属性
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		//向订单物流表中插入数据
		orderShippingMapper.insert(orderShipping);
		//返回结果
		return TaotaoResult.ok(orderId);
	}

}
