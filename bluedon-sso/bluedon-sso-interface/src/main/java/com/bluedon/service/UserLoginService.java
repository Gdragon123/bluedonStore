package com.bluedon.service;

import com.bluedon.common.pojo.TaotaoResult;

public interface UserLoginService {

	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	public TaotaoResult login(String username,String password);
	
	/**
	 * 根据token获取用户信息
	 * @param token
	 * @return
	 */
	public TaotaoResult getUserByToken(String token);
	
}
