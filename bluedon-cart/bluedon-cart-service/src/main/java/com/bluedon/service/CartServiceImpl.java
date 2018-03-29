package com.bluedon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.TbItem;
import com.bluedon.redis.JedisClient;
import com.bluedon.utils.JsonUtils;

@Service
public class CartServiceImpl implements CartService {

	@Value("${TT_CART_REDIS_PRE_KEY}")
	private String TT_CART_REDIS_PRE_KEY;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public TaotaoResult addCatItem(TbItem tbItem, String userId,Integer num) {
		//从redis中获取购物车
		TbItem item = queryTbItemByUserIdAndItemId(tbItem.getId(),userId);
		//判断是否为空
		if(item != null){
			//如果为不为空，商品数量相加
			item.setNum(item.getNum()+num);
			//设置到redis中
			jedisClient.hset(TT_CART_REDIS_PRE_KEY+":"+userId, item.getId()+"", JsonUtils.objectToJson(item));
		}else{
			//如果为空，直接添加商品到购物车
			tbItem.setNum(num);//设置购买数量
			if(StringUtils.isNotBlank(tbItem.getImage())){
				tbItem.setImage(tbItem.getImage().split(",")[0]);//将图片的内容设置成一张 
			}
			jedisClient.hset(TT_CART_REDIS_PRE_KEY+":"+userId, tbItem.getId()+"", JsonUtils.objectToJson(tbItem));
		}
		return TaotaoResult.ok();
	}
	
	/**
	 * 通过用户id和商品的id查询所对应的商品的数据，如果存在则不为空。
	 * @param itemId
	 * @param userId
	 * @return
	 */
	private TbItem queryTbItemByUserIdAndItemId(Long itemId, String userId){
		//注入jedisClient
		//获取数据
		String jsonstr = jedisClient.hget(TT_CART_REDIS_PRE_KEY+":"+userId, itemId+"");
		//判断是否为空
		if(StringUtils.isNotBlank(jsonstr)){
			//如果不为空，转成pojo对象
			TbItem item = JsonUtils.jsonToPojo(jsonstr, TbItem.class);
			//返回
			return item;
		}else{
			//如果为空，返回空
			return null;
		}
	}

	@Override
	public List<TbItem> getCartList(Long userId) {
		//根据key,获取redis中的购物车列表
		Map<String,String> map = jedisClient.hgetAll(TT_CART_REDIS_PRE_KEY+":"+userId);
		List<TbItem> list = new ArrayList<>();
		//判断是否为空
		if(map != null){
			//如果不为空，遍历购物车列表
			for (Map.Entry<String, String> entry : map.entrySet()) {
				//将json数据转成pojo对象
				TbItem item = JsonUtils.jsonToPojo(entry.getValue(), TbItem.class);
				//添加到list中
				list.add(item);
			}
			return list;
		}
		return list;//为了不再调用时进行非空判断，可以返回空的list
	}

	@Override
	public TaotaoResult updateCatByItemId(Long userId, Long itemId, Integer num) {
		//查询redis中用户对应的商品数据
		TbItem item = queryTbItemByUserIdAndItemId(itemId, userId+"");
		//判断是否为空
		if(item != null){
			//如果不为空，修改商品数量
			item.setNum(num);
			//重新存储到redis中
			jedisClient.hset(TT_CART_REDIS_PRE_KEY+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		}
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult deleteCatByUserIdItemId(Long userId, Long itemId) {
		//删除redis中用户对应的商品数据
		jedisClient.hdel(TT_CART_REDIS_PRE_KEY+":"+userId, itemId+"");
		return TaotaoResult.ok();
	}

}
