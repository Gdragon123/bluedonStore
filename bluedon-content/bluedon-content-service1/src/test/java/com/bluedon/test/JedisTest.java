package com.bluedon.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bluedon.redis.JedisClient;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	//测试单机版
	@Test
	public void testJedis(){
		
		//1.创建连接
		Jedis jedis = new Jedis("192.168.25.133", 7001);
		
		//2.获取jedis对象
		//3.通过redis命令进行设置
		jedis.set("testkey", "test");
		
		//4.获取打印
		String str = jedis.get("testkey");
		System.out.println(str);
		
		//5.关闭
		jedis.close();
		
	}
	
	//连接池
	@Test
	public void testJedisPool(){
		
		//1.创建jedispool对象，指定端口和ip地址
		JedisPool jedisPool = new JedisPool("192.168.25.133", 7001);
		
		//2.获取jedis对象
		Jedis jedis = jedisPool.getResource();
		
		//3.设置值
		jedis.set("poolkey", "poolvalue");
		
		//4.获取值
		String str = jedis.get("poolkey");
		
		//5.打印
		System.out.println(str);
		
		//6.关闭jedis（释放资源）
		jedis.close();
		
		//7.关闭连接池
		jedisPool.close(); //当应用停机的时候关闭
		
	}
	
	//测试集群
	@Test
	public void testCluster(){
		
		Set<HostAndPort> nodes = new HashSet<>();
		
		nodes.add(new HostAndPort("192.168.25.133", 7001));
		nodes.add(new HostAndPort("192.168.25.133", 7002));
		nodes.add(new HostAndPort("192.168.25.133", 7003));
		nodes.add(new HostAndPort("192.168.25.133", 7004));
		nodes.add(new HostAndPort("192.168.25.133", 7005));
		nodes.add(new HostAndPort("192.168.25.133", 7006));
		
		//1.创建连接
		JedisCluster jedisCluster = new JedisCluster(nodes);
		
		//2.设置值
		jedisCluster.set("clusterkey", "clustervalue");
		
		//3.获取值 并打印
		System.out.println(jedisCluster.get("clusterkey"));
		
		//4.关闭连接
		jedisCluster.close();
		
	}
	
	//spring管理redis
	@Test
	public void testSpringRedis(){
		//1.加载spring容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		
		//2.获取jedis对象
		JedisClient jedis = context.getBean(JedisClient.class);
		
		//3.设置值
		jedis.set("springkey", "springvalue");
		
		//4.获取值
		System.out.println(jedis.get("springkey"));
		
		
	}
	
	
	//测试集群
		@Test
		public void testSpringCluster(){
			
			//1.加载spring容器
			ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
			
			//2.获取jedis对象
			JedisClient jedis = context.getBean(JedisClient.class);
			
			//2.设置值
			jedis.set("clusterkey", "clustervalue");
			
			//3.获取值 并打印
			System.out.println(jedis.get("clusterkey"));
			
		}
}
