package com.bluedon.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.druid.sql.visitor.functions.Right;
import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.mapper.TbUserMapper;
import com.bluedon.pojo.TbUser;
import com.bluedon.pojo.TbUserExample;
import com.bluedon.pojo.TbUserExample.Criteria;
import com.bluedon.service.UserRegisterService;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

	@Autowired
	private TbUserMapper userMapper; 
	
	@Override
	public TaotaoResult checkData(String param, int type) {
		//从tb_user表中查询数据
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//查询条件根据参数动态生成
		//判断具体的type类型
		if(type==1){//用户名
			if(StringUtils.isBlank(param)){
				return TaotaoResult.ok(false);
			}else{
				criteria.andUsernameEqualTo(param);
			}
		}else if(type==2){//电话号码
			criteria.andPhoneEqualTo(param);
		}else if(type==3){//邮件
			criteria.andEmailEqualTo(param);
		}else{//其他不合法的参数
			return TaotaoResult.build(400, "非法参数");
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		//判断查询结果，如果查询到数据返回false
		if(list!=null && list.size()>0){
			return TaotaoResult.ok(false);
		}
		
		//查询到数据返回true
		return TaotaoResult.ok(true);
	}

	@Override
	public TaotaoResult register(TbUser user) {
		//判断用户名和密码是否为空
		if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())){
			//为空，直接返回错误
			return TaotaoResult.build(400, "注册失败.请校验数据后请再提交数据");
		}
		//不为空，检查用户名是否已被注册
		TaotaoResult result = checkData(user.getUsername(), 1);
		if(!(boolean)result.getData()){
			//如果已注册，返回提示
			return TaotaoResult.build(400, "用户名已经被注册");
		}
		//检查判断手机是否已被注册
		TaotaoResult result2 = checkData(user.getPhone(), 2);
		if(!(boolean) result2.getData()){
			//如果已注册，返回提示
			return TaotaoResult.build(400, "电话号码已经被注册");
		}
		//检查判断邮箱是否已被注册
		TaotaoResult result3 = checkData(user.getEmail(), 3);
		if(!(boolean) result3.getData()){
			//如果已注册，返回提示
			return TaotaoResult.build(400, "邮箱已经被注册");
		}
		//补全其他属性
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		//密码进行加密
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());//用spring提供的工具类进行加密
		user.setPassword(password);
		//插入数据
		userMapper.insertSelective(user);
		//返回注册结果的提示
		return TaotaoResult.ok();
	}

}
