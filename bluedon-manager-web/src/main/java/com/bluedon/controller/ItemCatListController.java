package com.bluedon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bluedon.common.pojo.EasyUITreeNode;
import com.bluedon.service.TbItemCatService;
import com.bluedon.utils.FastDFSClient;
import com.bluedon.utils.JsonUtils;


@Controller
public class ItemCatListController {
	
	@Autowired
	private TbItemCatService itemCatService;
	
	@Value("${FastDFS_IMAGE_UPLOAD}")
	private String FastDFS_IMAGE_UPLOAD;

	@RequestMapping("/item/cat/list")
	@ResponseBody		//因为第一次选中时，parentId为空，会出错，所以要给一个默认值defaultValue="0"
	public List<EasyUITreeNode> getItemCatList(@RequestParam(value="id",defaultValue="0") Long parentId){
		
		return itemCatService.getItemCatList(parentId);
		
	}
	
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody //默认的Content-Type:application/json;charset=UTF-8  火狐浏览器不支持这种json格式，要改为String类型
	public String pictureUpload(MultipartFile uploadFile) throws Exception{
		
		try {
			//获取文件拓展名
			String filename = uploadFile.getOriginalFilename();
			String suffxName = filename.substring(filename.lastIndexOf(".")+1);
			
			//创建FastDFS的客户端
			FastDFSClient fastDFSClient = new FastDFSClient("E:/Maven-JAVA-workSpace/taotao-web/src/main/resources/resource/fastDFS.conf");
			
			//执行上传处理
			String path = fastDFSClient.uploadFile(uploadFile.getBytes(), suffxName);
			
			//拼接返回的url和ip地址
			String url = FastDFS_IMAGE_UPLOAD + path;
			
			//封装map
			Map<String,Object> map = new HashMap<>();
			map.put("error", 0);
			map.put("url", url);
			
			//返回map  要将map对象转成json格式的字符串  用工具类
			String json = JsonUtils.objectToJson(map);
			
			return json;
			
		} catch (Exception e) {
			e.printStackTrace();
			
			Map<String,Object> map = new HashMap<>();
			map.put("error", 1);
			map.put("message", "图片上传失败");
			
			//返回map  要将map对象转成json格式的字符串  用工具类
			String json = JsonUtils.objectToJson(map);
			
			return json;
		}
		
		
	}
	
}
