package com.bluedon.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.bluedon.pojo.TbItem;

/**
 * 商品详情的pojo 展示图片的数组
 * @author Administrator
 *
 */
public class Item extends TbItem {

	
	public Item(){}
	
	public Item(TbItem item){
		BeanUtils.copyProperties(item, this);//使用beanutils拷贝pojo的所有属性到子类
	}
	
	public String[] getImages(){
		if(StringUtils.isNotBlank(this.getImage())){
			return this.getImage().split(",");
		}
		return null;
	}
	
}
