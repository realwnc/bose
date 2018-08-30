package com.mids.common.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

public class RedisUtil implements CacheClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
    // Redis服务器IP
    private static String HOST = "58.96.191.159";
    // Redis的端口号
    private static int PORT = 6379;
    // 访问密码
    private static String PASSWORD = "123456";
    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 1000;
    private static int MAX_TOTAL = 1000;

    // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT_TIME = 10000;
    private static int TIMEOUT = 10000;
    // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = false;
    private static boolean TEST_ON_RETURN = true;
    public static Pool<Jedis> jedisPool;
    //private final static Object object = new Object();
    private static final String PROPERTIES_FILE_PATH = "/sysconf/redis.properties";//"/busx_redis.properties";
    private static RedisUtil _instance = null;
    // Creates an syn object.
    private static Object SynObject = new Object();
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

    /**
     * 初始化Redis连接池
     */
    private RedisUtil() {

        try {
            initData();
            JedisPoolConfig config = new JedisPoolConfig();

            // 最大连接数, 应用自己评估，不要超过AliCloudDB for Redis每个实例最大的连接数
            config.setMaxTotal(MAX_TOTAL);
            // 最大空闲连接数, 应用自己评估，不要超过AliCloudDB for Redis每个实例最大的连接数
            config.setMaxIdle(MAX_IDLE);

            // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,
            // 默认-1
            config.setMaxWaitMillis(MAX_WAIT_TIME);
            // 在获取连接的时候检查有效性, 默认false
            // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的;
            config.setTestOnBorrow(TEST_ON_BORROW);
            // 在return给pool时，是否提前进行validate操作；
            config.setTestOnReturn(TEST_ON_RETURN);
            // 如果为true，表示有一个idle object evitor线程对idle
            // object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
            config.setTestWhileIdle(true);
            // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
            config.setNumTestsPerEvictionRun(MAX_IDLE);
            // 表示idle object evitor两次扫描之间要sleep的毫秒数；
            config.setTimeBetweenEvictionRunsMillis(1000);
            if (sentinelMaster != null) {

                if (sentinelSet != null && sentinelSet.size() > 0) {
                    jedisPool = new JedisSentinelPool(sentinelMaster, sentinelSet, config, TIMEOUT, PASSWORD);
                }
                else {
                	LOGGER.error("--------->Error configuring Redis Sentinel connection pool: expected both `sentinelMaster` and `sentiels` to be configured");
                }

            }
            else {
                jedisPool = new JedisPool(config, HOST, PORT, TIMEOUT, PASSWORD);
            }

        }
        catch (Exception e) {
        	LOGGER.error("--------->init jedis connect pool error, exception!!!", e);
        }

    }


    /**
     * 初始化各种需要的数据
     * 
     * @throws Exception
     */
    private static void initData() throws Exception {
        InputStream in = null;
        try {
            in = RedisUtil.class.getResourceAsStream(PROPERTIES_FILE_PATH);
            Properties propertie = new Properties();
            propertie.load(in);
            
            if (propertie.getProperty("redis.ip") != null) {
                HOST = String.valueOf(propertie.getProperty("redis.ip").trim());
            }
            if (propertie.getProperty("redis.port") != null && !"".equals(propertie.getProperty("redis.port").trim())) {
                PORT = Integer.valueOf(propertie.getProperty("redis.port").trim());
            }
            PASSWORD = String.valueOf(propertie.getProperty("redis.passwd").trim());
            TIMEOUT = Integer.valueOf(propertie.getProperty("redis.timeout").trim());
            MAX_WAIT_TIME = Integer.valueOf(propertie.getProperty("redis.maxWaitMillis").trim());
            MAX_TOTAL = Integer.valueOf(propertie.getProperty("redis.maxTotal").trim());
            MAX_IDLE = Integer.valueOf(propertie.getProperty("redis.maxIdle").trim());

            if (propertie.getProperty("sentinelMaster") != null) {
                sentinelMaster = String.valueOf(propertie.getProperty("sentinelMaster").trim());
            }
            if (propertie.getProperty("sentinels") != null) {
                sentinels = String.valueOf(propertie.getProperty("sentinels").trim());
            }
            
            setSentinels(sentinels);
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
                	LOGGER.error("--------->close InputStream error while initData!!!", e);
                }
            }
        }
    }
    

    public static RedisUtil getInstance() {

        // Double-Checked Locking
        if (null == _instance) {
            synchronized (SynObject) {
                if (null == _instance) {
                    _instance = new RedisUtil();
                }
            }
        }
        return _instance;

    }

    /**
     * 获取Jedis实例
     * 
     * @return
     */
    private static Jedis getJedis() {//
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
        	LOGGER.error("---------->get instance of jedis error!!!", e);
            return null;
        }
    }

    public String getString(final String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
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
                    jedis = getJedis();
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
            jedis = getJedis();
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
                    jedis = getJedis();
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
    public boolean setObject(final String key, final int seconds, final Object value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
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

    public boolean addObject(final String key, final Object value) {
        return setObject(key, value);
    }

    /*
     * 保存对象值 操作的时候key value都得是数组
     */
    public boolean setObject(final String key, final Object value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
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
                    jedis = getJedis();
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

    public String setString(final String key, final int seconds, final String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
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
    /* 
     * */
    public String setString(final String key, final String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if(jedis!=null){            	
            	return jedis.set(key, value);
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
    
    /* 只能设一次，也就是不存在时只能成功
     * Integer reply, specifically: 1 if the key was set 0 if the key was not set
     * */
    public Long setStringOnce(final String key, final String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if(jedis!=null){            	
            	return jedis.setnx(key, value);
            }else{
            	return 0L;
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
        return 0L;
    }

    public boolean delete(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
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
            jedis = getJedis();
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
            jedis = getJedis();
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
    
    
    // add by wncheng    
    /*
     * 保存对象值 操作的时候key value都得是数组
     */
    public int addMap(Map<String, String> map, final int seconds) {
        Jedis jedis = null;
        int nCnt=0;
        try {
            jedis = getJedis();
            String result =null;
            if(jedis!=null)
            {
            	for(Map.Entry<String, String> ety: map.entrySet())
    			{
            		result=jedis.setex(ety.getKey(), seconds, ety.getValue());
            		if (result != null && !"".equals(result))
            		{
            			nCnt++;
            		}
            		
    			}	
            }           
        }
        catch (Exception e) {
            if (jedis != null) {
                //jedisPool.returnBrokenResource(jedis);
            	jedis.close();
                jedis = null;
                try {// 重试多一次
                    jedis = getJedis();
                    String result = null;
                    if(jedis!=null)
                    {
                    	for(Map.Entry<String, String> ety: map.entrySet())
            			{
                    		result=jedis.setex(ety.getKey(), seconds, ety.getValue());
                    		if (result != null && !"".equals(result))
                    		{
                    			nCnt++;
                    		}
                    		
            			}	
                    }
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
        return nCnt;
    }
}