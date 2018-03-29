package com.bluedon.test;


import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.bluedon.utils.FastDFSClient;


public class FastDFStest {

	@Test
	public void testUpload1() throws Exception{
		
		//创建一个配置文件，配置上传的地址和端口		
		//初始化配置文件
		ClientGlobal.init("E:/Maven-JAVA-workSpace/taotao-web/src/main/resources/resource/fastDFS.conf");
		
		TrackerClient trackerClient = new TrackerClient();
		// 3、使用TrackerClient对象创建连接，获得一个TrackerServer对象。
		TrackerServer trackerServer = trackerClient.getConnection();
		// 4、创建一个StorageServer的引用，值为null
		StorageServer storageServer = null;
		// 5、创建一个StorageClient对象，需要两个参数TrackerServer对象、StorageServer的引用
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		
		//上传文件
		String[] upload_file = storageClient.upload_file("C:/Users/Administrator/Pictures/303619.jpg", "jpg", null);
		
		//打印图片的生成路径
		for (String string : upload_file) {
			System.out.println(string);
		}
		
	}
	
	
	@Test
	public void testFastDFSutil() throws Exception{
		
		FastDFSClient fastDFSClient = new FastDFSClient("E:/Maven-JAVA-workSpace/taotao-web/src/main/resources/resource/fastDFS.conf");
		String file = fastDFSClient.uploadFile("C:/Users/Administrator/Pictures/192178.jpg", "jpg");
		System.out.println(file);
		
	}
	
}
