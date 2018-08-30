package com.mids.redis.single;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

public class RedisUtilFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtilFactory.class);
	
	// Redis服务器IP
    private static String host = "58.96.191.159";
    // Redis的端口号
    private static int port = 6379;
    // 访问密码
    private static String password = "123456";
    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int max_idel = 1000;
    private static int max_total = 1000;

    // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int max_wait_time = 10000;
    private static int timeout = 10000;
    // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean test_on_borrow = false;
    private static boolean test_on_return = true;
    
    private static final String PROPERTIES_FILE_PATH = "/sysconf/redis.properties";//"/busx_redis.properties";
    private static RedisUtilFactory _instance = null;
	//---------------------------------------------
	private static JedisPool jedisPool = null;
	
	
	public RedisUtilFactory(){
		try
		{
			readConfig();
		}
		catch (Exception e) {
        	LOGGER.error("--------->init jedis connect pool error, exception!!!", e);
        }
		JedisPoolConfig config = new JedisPoolConfig();
        // 最大连接数, 应用自己评估，不要超过AliCloudDB for Redis每个实例最大的连接数
        config.setMaxTotal(max_total);
        // 最大空闲连接数, 应用自己评估，不要超过AliCloudDB for Redis每个实例最大的连接数
        config.setMaxIdle(max_idel);

        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,
        // 默认-1
        config.setMaxWaitMillis(max_wait_time);
        // 在获取连接的时候检查有效性, 默认false
        // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的;
        config.setTestOnBorrow(test_on_borrow);
        // 在return给pool时，是否提前进行validate操作；
        config.setTestOnReturn(test_on_return);
        // 如果为true，表示有一个idle object evitor线程对idle
        // object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
        config.setTestWhileIdle(true);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        config.setNumTestsPerEvictionRun(max_idel);
        // 表示idle object evitor两次扫描之间要sleep的毫秒数；
        config.setTimeBetweenEvictionRunsMillis(1000);
		jedisPool = new JedisPool(config, host, port, timeout, password);

	}
	private static void readConfig() throws Exception {
        InputStream in = null;
        try {
            in = RedisUtilFactory.class.getResourceAsStream(PROPERTIES_FILE_PATH);
            Properties propertie = new Properties();
            propertie.load(in);
            
            if (propertie.getProperty("redis.ip") != null) {
                host = String.valueOf(propertie.getProperty("redis.ip").trim());
            }
            if (propertie.getProperty("redis.port") != null && !"".equals(propertie.getProperty("redis.port").trim())) {
                port = Integer.valueOf(propertie.getProperty("redis.port").trim());
            }
            password = String.valueOf(propertie.getProperty("redis.passwd").trim());
            timeout = Integer.valueOf(propertie.getProperty("redis.timeout").trim());
            max_wait_time = Integer.valueOf(propertie.getProperty("redis.maxWaitMillis").trim());
            max_total = Integer.valueOf(propertie.getProperty("redis.maxTotal").trim());
            max_idel = Integer.valueOf(propertie.getProperty("redis.maxIdle").trim());
        }
        catch (Exception e) {
        	LOGGER.error("--------->init data while redis needed error!!!", e);
            throw e;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                	LOGGER.error("--------->close InputStream error while readConfig!!!", e);
                }
            }
        }
    }
	public static RedisUtilFactory getInstance() {

        // Double-Checked Locking
        if (null == _instance) {
        	_instance = new RedisUtilFactory();
        }
        return _instance;

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
        	LOGGER.error("---------->getString by key error!!!", e);
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
    public Object getObject(final String key) {
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
        	LOGGER.error("---------->getObject by key error!!!", e);
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
    public boolean setObject(final String key, final int seconds, final Object value) {
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
        return setObject(key, value);
    }

    /*
     * 保存对象值 操作的时候key value都得是数组
     */
    public boolean setObject(final String key, final Object value) {
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
        	LOGGER.error("---------->setObject by key error!!!", e);
        }
        finally {
            if (jedis != null) {
                //jedisPool.returnResource(jedis);
            	jedis.close();
            }
        }
        return false;
    }

    public String setString(final String key, final int seconds, final String value) {
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
        	LOGGER.error("---------->setString by key and seconds error!!!", e);
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
        	LOGGER.error("---------->delete by key error!!!", e);
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
        	LOGGER.error("---------->dels by pattern error!!!", e);
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
    public List<String> getKeys(String pattern) {

        if (pattern == null || "".equals(pattern)) {
            pattern = "*";
        }
        else {
            pattern = pattern + "*";
        }
        List<String> keyList = new ArrayList<String>();
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
        	LOGGER.error("---------->getKeys by pattern error!!!", e);
        }
        finally {
            if (jedis != null) {
                //jedisPool.returnResource(jedis);
            	jedis.close();
            }
        }
        return keyList;
    }

   
}
