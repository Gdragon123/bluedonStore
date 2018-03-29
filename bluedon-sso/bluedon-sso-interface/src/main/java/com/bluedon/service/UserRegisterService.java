package com.bluedon.service;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.TbUser;

/**
 * 用户注册的接口
 * @author Administrator
 *
 */
public interface UserRegisterService {

	/**
	 * 检查用户是否已注册
	 * @param param 要校验的数据
	 * @param type 校验的数据类型
	 * @return 可选参数1、2、3分别代表username、phone、email
	 */
	public TaotaoResult checkData(String param, int type);
	
	/**用户注册
	 * @param user
	 * @return
	 */
	public TaotaoResult register(TbUser user);
	
}
