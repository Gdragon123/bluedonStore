package com.bluedon.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bluedon.mapper.TbItemMapper;
import com.bluedon.pojo.TbItem;
import com.bluedon.pojo.TbItemDesc;
import com.bluedon.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TestPageHelper {

	@Test
	public void test(){
		
		//1.初始化spring容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		
		//2.获取spring容器中管理mapper的代理对象
		TbItemMapper mapper = context.getBean(TbItemMapper.class);
		
		//3.设置分页信息（紧跟着的第一个select 才会被分页）
		PageHelper.startPage(1, 5);
		
		//4.获取分页信息
		TbItemExample example = new TbItemExample();
		
		List<TbItem> list1 = mapper.selectByExample(example);
		
		System.out.println(list1.size());
		
		//获取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list1);
		System.out.println("总记录数 》》"+pageInfo.getTotal());
		
		//打印结果
		for (TbItem tbItem : list1) {
			System.out.println(tbItem.getTitle());
		}
	}
	
}
