package com.bluedon.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.mapper.TbUserMapper;
import com.bluedon.pojo.TbUser;
import com.bluedon.pojo.TbUserExample;
import com.bluedon.pojo.TbUserExample.Criteria;
import com.bluedon.redis.JedisClient;
import com.bluedon.service.UserLoginService;
import com.bluedon.utils.JsonUtils;

@Service
public class UserLoginServiceImpl implements UserLoginService {

	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${USER_INFO}")
	private String USER_INFO;
	
	@Value("${EXPIRE}")
	private int EXPIRE;
	
	@Override
	public TaotaoResult login(String username, String password) {
		//判断用户名和密码是否为空
		if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
			//如果为空，直接返回错误提示
			return TaotaoResult.build(400, "用户名和密码不能为空");
		}
		//设置查询条件为用户名
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		//判断结果是否为空
		if(list == null || list.size() == 0){
			//如果为空，直接返回错误提示
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		//获取查询到的用户
		TbUser user = list.get(0);
		//判断用户输入的密码是否和查询到的相同
		if(!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
			//如果不同，直接返回错误提示
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		//登录成功，生成token
		String token = UUID.randomUUID().toString();
		//修改密码为空
		user.setPassword(null);//为了不让用户看到
		//将用户信息存到redis中
		jedisClient.set(USER_INFO+":"+token, JsonUtils.objectToJson(user));
		//设置有效期
		jedisClient.expire(USER_INFO+":"+token, EXPIRE);
		//返回taotaoResult，包括token
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		//注入jedisClient
		//获取redis中的token
		String jsonstr = jedisClient.get(USER_INFO+":"+token);
		//判断是否为空
		if(StringUtils.isNoneBlank(jsonstr)){
			//重置key的过期时间
			jedisClient.expire(USER_INFO+":"+token, EXPIRE);
			//如果不为空，将json数据转为对象
			TbUser user = JsonUtils.jsonToPojo(jsonstr, TbUser.class);
			//返回用户数据
			return TaotaoResult.ok(user);
		}else{
			//如果为空，返回过期提示
			return TaotaoResult.build(400, "用户登录已经过期，请重新登录");
		}
	}

}
