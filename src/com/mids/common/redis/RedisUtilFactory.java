package com.mids.common.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

public class RedisUtilFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
	
	private String host = "127.0.0.1";
	private int port = 6379;
	private String password = "123456";	
	//timeout for jedis try to connect to redis server, not expire time! In milliseconds
	private int timeout = 0;
	
	//private int maxIdle = 1000;
	//private int maxTotal = 1000;
	 // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    //private int maxWaitTime = 10000;
	//private boolean testOnBorrow = false;
    //private boolean testOnReturn = true;
    private String database;
	
	private JedisPoolConfig poolConfig;	

	private static Pool<Jedis> jedisPool = null;
	
	// Creates an syn object.
    //private static Object SynObject = new Object();
    protected static String sentinelMaster = null;
    protected static String sentinels = null;
    protected static Set<String> sentinelSet = null;

	public static void setSentinels(String sentinels) {
        if (null == sentinels) {
            sentinels = "";
        }

        String[] sentinelArray = sentinels.split(",");
        sentinelSet = new HashSet<String>(Arrays.asList(sentinelArray));
    }

	public JedisPoolConfig getPoolConfig() {
		return poolConfig;
	}
	public void setPoolConfig(JedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}
	
	public RedisUtilFactory(){
		
	}
	
	/**
	 * 初始化方法
	 */
	public void init(){
		setSentinels(sentinels);		
		/*
		JedisPoolConfig config = new JedisPoolConfig();
		// 最大连接数, 应用自己评估，不要超过AliCloudDB for Redis每个实例最大的连接数
        config.setMaxTotal(maxTotal);
        // 最大空闲连接数, 应用自己评估，不要超过AliCloudDB for Redis每个实例最大的连接数
        config.setMaxIdle(maxIdle);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,
        // 默认-1
        config.setMaxWaitMillis(maxWaitTime);
        // 在获取连接的时候检查有效性, 默认false
        // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的;
        config.setTestOnBorrow(testOnBorrow);
        // 在return给pool时，是否提前进行validate操作；
        config.setTestOnReturn(testOnReturn);
        // 如果为true，表示有一个idle object evitor线程对idle
        // object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
        config.setTestWhileIdle(true);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        config.setNumTestsPerEvictionRun(maxIdle);
        // 表示idle object evitor两次扫描之间要sleep的毫秒数；
        config.setTimeBetweenEvictionRunsMillis(1000);
		*/
		if (sentinelMaster != null) {
			if (sentinelSet != null && sentinelSet.size() > 0) {
				jedisPool = new JedisSentinelPool(sentinelMaster, sentinelSet, poolConfig, timeout, password);
			}
			else {
				LOGGER.error("--------->Error configuring Redis Sentinel connection pool: expected both `sentinelMaster` and `sentiels` to be configured");
			}

		}
		else {
			if(jedisPool == null){
				if(password != null && !"".equals(password)){
					jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password);
				}else if(timeout != 0){
					jedisPool = new JedisPool(new JedisPoolConfig(), host, port,timeout);
				}else{
					jedisPool = new JedisPool(new JedisPoolConfig(), host, port);
				}				
			}
		}

		if (sentinelMaster != null) {
			if (sentinelSet != null && sentinelSet.size() > 0) {
				jedisPool = new JedisSentinelPool(sentinelMaster, sentinelSet, poolConfig, timeout, password);
			}
			else {
				LOGGER.error("--------->Error configuring Redis Sentinel connection pool: expected both `sentinelMaster` and `sentiels` to be configured");
			}

		}
		else {
			jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
		}
	}
	/**
	 * 初始化方法
	 */
	public void destroy(){
		
	}
	
	public String getString(final String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if(jedis!=null){
            return jedis.get(key);
            }
        }
        catch (Exception e) {
            if (jedis != null) {
                //jedisPool.returnBrokenResource(jedis);
            	jedis.close();
                jedis = null;
                try {
                    // 会出现这样的错Software caused connection abort: recv failed再试多一次
                	jedis = jedisPool.getResource();
                    return jedis.get(key);
                }
                catch (Exception ee) {
                    if (jedis != null) {
                        //jedisPool.returnBrokenResource(jedis);
                    	jedis.close();
                        jedis = null;
                    }
                    LOGGER.error("---------->getString by key error!!!", ee);
                }
            }

        }
        finally {
            if (jedis != null) {
                //jedisPool.returnResource(jedis);
            	jedis.close();
            }
        }
        return "";
    }

    /*
     * 获取对象值 操作的时候key value都得是数组
     */
    public Object get(final String key) {
        Jedis jedis = null;
        try {
        	jedis = jedisPool.getResource();
            byte[] object =null;
            if(jedis!=null){
            	object=jedis.get(key.getBytes());
            	}
            return SerializeUtil.unserialize(object);
        }
        catch (JedisException e) {
            if (jedis != null) {
                //jedisPool.returnBrokenResource(jedis);
            	jedis.close();
                jedis = null;
                try {
                    // 会出现这样的错Software caused connection abort: recv failed再试多一次
                	jedis = jedisPool.getResource();
                    byte[] object = jedis.get(key.getBytes());
                    return SerializeUtil.unserialize(object);
                }
                catch (Exception ee) {
                    if (jedis != null) {
                        //jedisPool.returnBrokenResource(jedis);
                    	jedis.close();
                        jedis = null;
                    }
                    LOGGER.error("---------->getObject by key error!!!", ee);
                }
            }

        }
        finally {
            if (jedis != null) {
                //jedisPool.returnResource(jedis);
            	jedis.close();
            }
        }
        return null;
    }

    /*
     * 第一个参数 key 第二个参数 有效时间 单位秒 第三个参数 值 保存对象值 操作的时候key value都得是数组
     */
    public boolean set(final String key, final int seconds, final Object value) {
        Jedis jedis = null;
        try {
        	jedis = jedisPool.getResource();
            String result=null;
            if(jedis!=null){            	
            	result = jedis.setex(key.getBytes(), seconds, SerializeUtil.serialize(value));
            }
            if (result != null && !"".equals(result)) { return true; }
        }
        catch (JedisException e) {
            if (jedis != null) {
                //jedisPool.returnBrokenResource(jedis);
            	jedis.close();
                jedis = null;
            }
            LOGGER.error("---------->setObject by key and seconds error!!!", e);
        }
        finally {
            if (jedis != null) {
                //jedisPool.returnResource(jedis);
            	jedis.close();
            }
        }
        return false;
    }

    public boolean add(final String key, final Object value) {
        return set(key, value);
    }

    /*
     * 保存对象值 操作的时候key value都得是数组
     */
    public boolean set(final String key, final Object value) {
        Jedis jedis = null;
        try {
        	jedis = jedisPool.getResource();
            String result =null;
            if(jedis!=null){
            	result=jedis.set(key.getBytes(), SerializeUtil.serialize(value));
            }
            if (result != null && !"".equals(result)) { return true; }
        }
        catch (Exception e) {
            if (jedis != null) {
                //jedisPool.returnBrokenResource(jedis);
            	jedis.close();
                jedis = null;
                try {// 重试多一次
                	jedis = jedisPool.getResource();
                    String result = null;
                    if(jedis!=null){
                    	result=jedis.set(key.getBytes(), SerializeUtil.serialize(value));
                    }
                    if (result != null && !"".equals(result)) { return true; }
                }
                catch (Exception ee) {
                    if (jedis != null) {
                        //jedisPool.returnBrokenResource(jedis);
                    	jedis.close();
                        jedis = null;
                    }
                    LOGGER.error("---------->setObject by key error!!!", ee);
                }

            }

        }
        finally {
            if (jedis != null) {
                //jedisPool.returnResource(jedis);
            	jedis.close();
            }
        }
        return false;
    }

    public String set(final String key, final int seconds, final String value) {
        Jedis jedis = null;
        try {
        	jedis = jedisPool.getResource();
            if(jedis!=null){            	
            	return jedis.setex(key, seconds, value);
            }else{
            	return "";
            }
        }
        catch (JedisException e) {
            if (jedis != null) {
                //jedisPool.returnBrokenResource(jedis);
            	jedis.close();
                jedis = null;
            }
            LOGGER.error("set出错", e);
        }
        finally {
            if (jedis != null) {
                //jedisPool.returnResource(jedis);
            	jedis.close();
            }
        }
        return "";
    }

    public boolean delete(String key) {
        Jedis jedis = null;
        try {
        	jedis = jedisPool.getResource();
            long result =-1;
            if(jedis!=null){            	
            	result=jedis.del(key);
            }
            if (result != -1) {
                return true;
            }
            else {
            	LOGGER.error("---------->error on delete by key="+key);
            }
        }
        catch (JedisException e) {
            if (jedis != null) {
                //jedisPool.returnBrokenResource(jedis);
            	jedis.close();
                jedis = null;
            }
            LOGGER.error("del出错", e);
        }
        finally {
            if (jedis != null) {
                //jedisPool.returnResource(jedis);
            	jedis.close();
            }
        }
        return false;
    }

    public void dels(String pattern) {
        if (pattern == null || pattern.equals("")) return;
        if (!pattern.endsWith("*")) pattern += "*";

        Jedis jedis = null;
        try {
        	jedis = jedisPool.getResource();
            if(jedis!=null){            	
            	Set<String> keys = jedis.keys(pattern);            	
            	Iterator<String> it = keys.iterator();
            	while (it.hasNext()) {
            		String key = it.next();
            		jedis.del(key);
            	}
            }
        }
        catch (JedisException e) {
            if (jedis != null) {
                //jedisPool.returnBrokenResource(jedis);
            	jedis.close();
                jedis = null;
            }
            LOGGER.error("dels出错", e);
        }
        finally {
            if (jedis != null) {
                //jedisPool.returnResource(jedis);
            	jedis.close();
            }
        }
    }

    /*
     * 获取缓存的所有的KEY值
     */
    public List getKeys(String pattern) {

        if (pattern == null || "".equals(pattern)) {
            pattern = "*";
        }
        else {
            pattern = pattern + "*";
        }
        List<String> keyList = new ArrayList();
        Jedis jedis = null;
        try {
        	jedis = jedisPool.getResource();
            if(jedis!=null){
            Set<String> keys = jedis.keys(pattern);

            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                keyList.add(key);
            }
            }
        }
        catch (JedisException e) {
            if (jedis != null) {
                //jedisPool.returnBrokenResource(jedis);
            	jedis.close();
                jedis = null;
            }
            LOGGER.error("dels出错", e);
        }
        finally {
            if (jedis != null) {
                //jedisPool.returnResource(jedis);
            	jedis.close();
            }
        }
        return keyList;
    }

    /**
     * 释放jedis资源
     * 
     * @param jedis
     */
    /*
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            //jedisPool.returnResource(jedis);
        	jedis.close();
        }
    }
	*/
	
    
    
    
    
    // setter and getter
    public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	/*
	public int getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	public int getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(int maxTotal) {
		maxTotal = maxTotal;
	}
	public int getMaxWaitTime() {
		return maxWaitTime;
	}
	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}
	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	public boolean isTestOnReturn() {
		return testOnReturn;
	}
	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}
	*/
	 public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	
	public static String getSentinelMaster() {
		return sentinelMaster;
	}
	public static void setSentinelMaster(String sentinelMaster) {
		RedisUtilFactory.sentinelMaster = sentinelMaster;
	}
	public static String getSentinels() {
		return sentinels;
	}

}
