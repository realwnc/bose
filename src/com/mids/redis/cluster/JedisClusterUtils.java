package com.mids.redis.cluster;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClusterUtils{
	private static final Logger LOGGER = LoggerFactory.getLogger(JedisClusterUtils.class);
	//----- config
	private static Integer maxTotal=100;
	private static Integer maxIdel=50;
	private static Integer minIdel=20;
	private static Long maxWaitMillis=6 * 1000L;
	//-----
	private static JedisCluster jedisCluster=null;
	private static Integer timeout=2000;
	private static Integer maxRedirections=100;
	//private GenericObjectPoolConfig genericObjectPoolConfig;
	private static String clusterNodes="10.93.10.237:6379,10.93.10.237:6379";
	
	public JedisClusterUtils()
	{
	}
	public static void initJedisCluster()
	{
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdel);
		config.setMinIdle(minIdel);
		config.setMaxWaitMillis(maxWaitMillis);
		config.setTestOnBorrow(true);
		
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		String[] nodes = clusterNodes.split(",");
		for(String node : nodes)
		{
			String[] hosts = node.split(":");
			if(hosts.length<2)
			{
				continue;
			}
			String ip = hosts[0];
			Integer port = Integer.parseInt(hosts[1]);
			
			jedisClusterNodes.add(new HostAndPort(ip, port));
			
			LOGGER.info("--------->HostAndPort add ip={}, port={}", ip, port);
		}

		//jedisClusterNodes.add(new HostAndPort("192.168.1.112", 7112));
		
		jedisCluster = new JedisCluster(jedisClusterNodes, timeout, maxRedirections, config);
	}
	public static JedisCluster getJedisCluster() throws Exception {
		if(jedisCluster==null)
		{
			initJedisCluster();
		}
		return jedisCluster;
	}


}