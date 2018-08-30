package com.mids.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mids.ConstantsCode;
import com.mids.util.MyFastjsonUtil;

public class HttpsClientCommon {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpsClientCommon.class);
	
    /** 连接超时时间 */
    private static int defaultConnectionTimeout=8000;//8 sec
    /** 回应超时时间,缺省为30秒钟 */
    private static int defaultSoTimeout=30000;//30 sec
    private static int defaultMaxConnPerHost=1000;
    private static int defaultMaxTotalConn=1000;
    private static int closeIdleConnectionsTime=0;
    /** 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：30秒 */
    private static int defaultHttpConnectionManagerTimeout=30000;//30 sec
    private static int keepAliveTimeout=30000;//30 sec
    /** 是否开启https */
    private static String useSSL="off";
    private static CloseableHttpClient httpClient = null;
    //private static final Object object = new Object();
    private static final String PROPERTIES_FILE_PATH = "/httputil.properties";
    
    private static String useKeystore="false";
    private static String keystoreFile="";
    private static String keystorePassword="";
    private static String keystoreHost="";
    
    //private static boolean bInitPool = false;
    static
    {
    	try
    	{
	    	readConfig();
	    	if(useKeystore=="true")
	    	{
	    		initSSLTrustStore();//证书初始化, client with keystore also
	    	}
	    	
	        HttpClientBuilder httpClientBuilder = HttpClients.custom();
	        // 设置各种超时时间
	        RequestConfig requestConfig = RequestConfig.custom()
	                .setConnectTimeout(defaultConnectionTimeout)
	                .setSocketTimeout(defaultSoTimeout)
	                .setConnectionRequestTimeout(defaultHttpConnectionManagerTimeout)
	                .build();
	        
	        ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {
	            @Override
	            public long getKeepAliveDuration(HttpResponse httpResponse, org.apache.http.protocol.HttpContext httpContext) {
	                return keepAliveTimeout; // tomcat默认keepAliveTimeout为20s
	            }
	        };
	        // 获取连接池管理器
	        PoolingHttpClientConnectionManager connManager = createConnectionManager();
	        httpClientBuilder.setDefaultRequestConfig(requestConfig);
	        httpClientBuilder.setConnectionManager(connManager);
	        httpClientBuilder.setKeepAliveStrategy(connectionKeepAliveStrategy);
	        httpClient = httpClientBuilder.build();
	        if(httpClient == null)
	        {
	        	LOGGER.error("--------->HttpsClientCommon init error......");
	        }
    	}
        catch(Exception e)
        {
        	LOGGER.error("--------->HttpsClientCommon init excption......");
        	e.printStackTrace();
        }
    }
    
    /**
     * 初始化各种需要的数据
     * 
     * @throws Exception
     */
    private static void readConfig() throws Exception {
    	InputStream in = null;
        try {
            in = HttpsClientCommon.class.getResourceAsStream(PROPERTIES_FILE_PATH);
            Properties propertie = new Properties();
            propertie.load(in);
            defaultConnectionTimeout = Integer.valueOf(propertie.getProperty("defaultConnectionTimeout"));
            defaultSoTimeout = Integer.valueOf(propertie.getProperty("defaultSoTimeout"));
            defaultMaxConnPerHost = Integer.valueOf(propertie.getProperty("defaultMaxConnPerHost"));
            defaultMaxTotalConn = Integer.valueOf(propertie.getProperty("defaultMaxTotalConn"));
            defaultHttpConnectionManagerTimeout = Integer.valueOf(propertie.getProperty("defaultHttpConnectionManagerTimeout"));
            closeIdleConnectionsTime = Integer.valueOf(propertie.getProperty("closeIdleConnectionsTime"));
            useSSL = propertie.getProperty("useSSL");
            
            useKeystore = propertie.getProperty("useKeystore");
            keystoreFile = propertie.getProperty("keystore.file");
            keystorePassword = propertie.getProperty("keystore.password");
            keystoreHost = propertie.getProperty("keystore.host");
        }
        catch (Exception e) {
            LOGGER.error("-------->error on read config="+PROPERTIES_FILE_PATH);
            e.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                	LOGGER.error("-------->error on close config="+PROPERTIES_FILE_PATH);
                	e.printStackTrace();
                }
            }
        }
    }
    
    private static int initSSLTrustStore()
    {
    	int ret = 0;
		try 
		{
			String classpath = HttpsClientCommon.class.getClassLoader().getResource("").getPath();
			String keystoreFilepath = classpath + keystoreFile;
			final String host = keystoreHost;			
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					if (host.equals(hostname)){
						return true;
					}
					return false;
				}
			});
			
			System.setProperty("javax.net.ssl.trustStore", keystoreFilepath);
            System.setProperty("javax.net.ssl.trustStorePassword",keystorePassword);
            System.setProperty("java.protocol.handler.pkgs","sun.net.www.protocol");
		} catch (Exception e) {
			ret = -1;
			System.out.println("establish connection error.");
			e.printStackTrace();
		}
		return ret;
    }
    
    /**
     * 初始化SSLConnectionSocketFactory(形成https访问的主要对象)
     * 
     * @return
     * @throws Exception
     */
    private static Registry<ConnectionSocketFactory> initSSLConnectionSocketFactory() throws Exception {
        // 信任所有
        X509TrustManager xtm = new X509TrustManager() { // 创建TrustManager
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        // TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
        SSLContext ctx = SSLContext.getInstance("TLS");
        // 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
        ctx.init(null, new TrustManager[] { xtm }, null);
        // 创建SSLConnectionSocketFactory对象
        //SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx);
		//SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
        // 注册SSL
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslsf).build();

        return socketFactoryRegistry;
    }

    private static PoolingHttpClientConnectionManager createConnectionManager() throws Exception {
        PoolingHttpClientConnectionManager conman = null;
        if ("true".equals(useSSL)) {
            Registry<ConnectionSocketFactory> registry = initSSLConnectionSocketFactory();
            conman = new PoolingHttpClientConnectionManager(registry);
        }
        else {
            conman = new PoolingHttpClientConnectionManager();
        }
        conman.setDefaultMaxPerRoute(defaultMaxConnPerHost);
        conman.setMaxTotal(defaultMaxTotalConn);
        conman.closeIdleConnections(0, TimeUnit.SECONDS);

        return conman;
    }

    /**
     * 发送https的post请求到指定的url(提交的数据为json数据)
     * 
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    public static String postJsonStr(String url, String data){
        HttpPost httpPost = new HttpPost(url);;
        String result = "";
        long start = System.currentTimeMillis();
        try {
            // 设置请求头信息
        	httpPost.setHeader("Content-Type", "application/json");
            // 将map转换成json然后发送
            String query = data;
            httpPost.setEntity(new StringEntity(query, "utf-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            //InputStream in = entity.getContent();
            result = EntityUtils.toString(entity, "utf-8");
            
            EntityUtils.consume(response.getEntity());
            //in.close();
            response.close();
        }
        catch (Exception e) {
        	LOGGER.error("--------->HttpsClientCommon postJsonStr Exception url={}", url);
        	e.printStackTrace();
        	result = "";
        }
        finally {
            // 释放连接
            if (httpPost != null) {
            	httpPost.releaseConnection();
            }
            long interval = System.currentTimeMillis() - start;
            LOGGER.debug("--------->HttpsClientCommon postJsonStr url={} elapsed time={} second", url, interval);
        }
        return result;
    }
    
    /**
     * 发送https的post请求到指定的url(提交的数据为json数据)
     * 
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    public static Map<String, Object> postJson(String url, Map<String, Object> data){
        HttpPost httpPost = new HttpPost(url);;
        Map<String, Object> resultmap = new HashMap<String, Object>();
        long start = System.currentTimeMillis();
        try {
            // 设置请求头信息
        	httpPost.setHeader("Content-Type", "application/json");
            // 将map转换成json然后发送
            String query = MyFastjsonUtil.map2Json(data);
            httpPost.setEntity(new StringEntity(query, "utf-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            //InputStream in = entity.getContent();
            String result = EntityUtils.toString(entity, "utf-8");
            resultmap = MyFastjsonUtil.json2Map(result);
            
            EntityUtils.consume(response.getEntity());
            //in.close();
            response.close();
        }
        catch (Exception e) {
        	LOGGER.error("--------->HttpsClientCommon postJson Exception url={}", url);
        	e.printStackTrace();
        	resultmap.put("retcode", ConstantsCode.RETCODE_EXCEPTION);
        	resultmap.put("retmsg", "Exception");
        }
        finally {
            // 释放连接
            if (httpPost != null) {
            	httpPost.releaseConnection();
            }
            long interval = System.currentTimeMillis() - start;
            LOGGER.debug("--------->HttpsClientCommon postData url={} elapsed time={} second", url, interval);
        }
        return resultmap;
    }
    /**
     * 通过post提交方式获取url指定的资源和数据
     * 
     * @param url
     * @param nameValuePairs
     *            请求参数
     * @return
     * @throws DajieHttpRequestException
     */
    public static String postData(String url, List<NameValuePair> nameValuePairs) {
        return postData(url, null, nameValuePairs);
    }

    /**
     * 通过post提交方式获取url指定的资源和数据
     * 
     * @param url
     * @param nameValuePairs
     *            请求参数
     * @param headers
     *            请求header参数
     * @return
     * @throws DajieHttpRequestException
     */
    public static String postData(String url, Map<String, String> headers, List<NameValuePair> nameValuePairs) 
    {
    	String result = "";
        long start = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
        try {
            if (headers != null && headers.size() > 0) {
                Set<Map.Entry<String, String>> set = headers.entrySet();
                for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
                    Map.Entry<String, String> header = it.next();
                    if (header != null) {
                        httpPost.setHeader(header.getKey(), header.getValue());
                    }
                }
            }
            if (nameValuePairs != null && nameValuePairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            }

            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            
            result = EntityUtils.toString(entity, "UTF-8");
            response.close();
        }
        catch (Exception e) {
        	LOGGER.error("--------->HttpsClientCommon postData Exception url={}", url);
        	e.printStackTrace();
        }
        finally {
        	// 释放连接
            if (httpPost != null) {
            	httpPost.releaseConnection();
            }
            long interval = System.currentTimeMillis() - start;
            LOGGER.debug("--------->HttpsClientCommon postData url={} elapsed time={} second", url, interval);
        }
        return result;
    }

    /**
     * 通过post提交方式获取url指定的资源和数据
     * 
     * @param url
     * @param nameValuePairs
     *            请求参数
     * @param headers
     *            请求header参数
     * @return
     * @throws DajieHttpRequestException
     */
    public static String postData(String url, Map<String, String> headers, Map<String, String> params) 
    {
    	String result = "";
        long start = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
        try {
            if (headers != null && headers.size() > 0) {
                Set<Map.Entry<String, String>> set = headers.entrySet();
                for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
                    Map.Entry<String, String> header = it.next();
                    if (header != null) {
                        httpPost.setHeader(header.getKey(), header.getValue());
                    }
                }
            }
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            if (params != null && params.size() > 0) {
                Set<Map.Entry<String, String>> set = params.entrySet();
                for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
                    Map.Entry<String, String> param = it.next();
                    if (param != null) {
                    	nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                    }
                }
            }
            if (nameValuePairs != null && nameValuePairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            }

            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            
            result = EntityUtils.toString(entity, "UTF-8");
            response.close();
        }
        catch (Exception e) {
        	LOGGER.error("--------->HttpsClientCommon postData Exception url={}", url);
        	e.printStackTrace();
        }
        finally {
        	// 释放连接
            if (httpPost != null) {
            	httpPost.releaseConnection();
            }
            long interval = System.currentTimeMillis() - start;
            LOGGER.debug("--------->HttpsClientCommon postData url={} elapsed time={} second", url, interval);
        }
        return result;
    }
    
    public static String postContent(String url, String content, String ContentType){
    	String response = "";
    	HttpPost httpPost = new HttpPost(url);
    	long start = System.currentTimeMillis();
    	try
		{
            
            StringEntity stringEntity = new StringEntity(content, "utf-8");
            stringEntity.setContentType("text/xml");
            if (ContentType != null &&  ContentType.length() > 0)
            {
            	httpPost.setHeader("Content-Type", ContentType);
            }
            httpPost.setEntity(stringEntity);
            long stbegin = System.currentTimeMillis();
            
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            long stendxx = System.currentTimeMillis();
            LOGGER.info("--------->HttpsClientCommon postContent consume time="+String.valueOf(stendxx-stbegin)+" ms");
            //WorkLog.logInfo("execute finish cost %s\n", System.currentTimeMillis()-st);
            HttpEntity entity = httpResponse.getEntity();//获取响应实体
            //WorkLog.logInfo("resopne:[%s]\n", entity.toString());
            //WorkLog.logInfo("resopne:[%s]\n", EntityUtils.toString(entity));
            response = EntityUtils.toString(entity);
            httpPost.releaseConnection();         
            httpResponse.close();
		}
    	catch(SocketTimeoutException e)
    	{
    		e.printStackTrace();
    		LOGGER.error("--------->HttpsClientCommon postContent SocketTimeoutException!!!");
    	}
    	catch(ConnectTimeoutException e)
    	{
    		e.printStackTrace();
    		LOGGER.error("--------->HttpsClientCommon postContent ConnectTimeoutException!!!");
    	}
		catch (Exception e)
		{
			LOGGER.error("--------->HttpsClientCommon postContent Exception!!!");
			e.printStackTrace();
		}
    	finally
    	{
    		if (httpPost != null) {
    			httpPost.releaseConnection();
    		}
    		long interval = System.currentTimeMillis() - start;
        	LOGGER.debug("--------->HttpsClientCommon postContent url={} elapsed time={} second", url, interval);
    	}
    	return response;
    }
    
    public static String postBinaryData(String url, Map<String, String> headers, String data){
    	String response = "";
    	HttpPost httpPost = new HttpPost(url);
    	long start = System.currentTimeMillis();
    	try
		{
    		if (headers != null && headers.size() > 0) {
                Set<Map.Entry<String, String>> set = headers.entrySet();
                for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
                    Map.Entry<String, String> header = it.next();
                    if (header != null) {
                    	httpPost.setHeader(header.getKey(), header.getValue());
                    }
                }
            }
            
            StringEntity stringEntity = new StringEntity(data, "utf-8");
            stringEntity.setContentType("binary");            
            httpPost.setEntity(stringEntity);
            long stbegin = System.currentTimeMillis();
            
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            long stendxx = System.currentTimeMillis();
            LOGGER.info("--------->HttpsClientCommon postBinaryData consume time="+String.valueOf(stendxx-stbegin)+" ms");
            //WorkLog.logInfo("execute finish cost %s\n", System.currentTimeMillis()-st);
            HttpEntity entity = httpResponse.getEntity();//获取响应实体
            //WorkLog.logInfo("resopne:[%s]\n", entity.toString());
            //WorkLog.logInfo("resopne:[%s]\n", EntityUtils.toString(entity));
            response = EntityUtils.toString(entity);
            httpPost.releaseConnection();         
            httpResponse.close();
		}
    	catch(SocketTimeoutException e)
    	{
    		e.printStackTrace();
    		LOGGER.error("--------->HttpsClientCommon postBinaryData SocketTimeoutException!!!");
    	}
    	catch(ConnectTimeoutException e)
    	{
    		e.printStackTrace();
    		LOGGER.error("--------->HttpsClientCommon postBinaryData ConnectTimeoutException!!!");
    	}
		catch (Exception e)
		{
			LOGGER.error("--------->HttpsClientCommon postBinaryData Exception!!!");
			e.printStackTrace();
		}
    	finally
    	{
    		if (httpPost != null) {
    			httpPost.releaseConnection();
    		}
    		long interval = System.currentTimeMillis() - start;
        	LOGGER.debug("--------->HttpsClientCommon postBinaryData url={} elapsed time={} second", url, interval);
    	}
    	return response;
    }
    
    
    //------------------------------- add by wncheng ---------------------------------------------------
    public static String doGetUrl(String url)
    {
    	String response="";
    	HttpGet httpGet = new HttpGet(url);
    	long start = System.currentTimeMillis();
    	try
    	{
            //用get方法发送http请求    		
    		System.out.println("--------->HttpsClientCommon get uri="+httpGet.getURI());
        	CloseableHttpResponse httpResponse =null;
        	HttpEntity entity=null;
        	if(httpClient!=null&&httpGet!=null){
        		httpResponse=httpClient.execute(httpGet);
        		entity= httpResponse.getEntity();
        	}
        	if (null != entity)
        	{
        		//response=EntityUtils.toString(entity);
        		response = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        		LOGGER.info("--------->HttpsClientCommon response code={}, content={}", httpResponse.getStatusLine(), response);
        	}
        	httpResponse.close();
       
    	} catch (Exception e) 
		{
    		LOGGER.error("--------->HttpsClientCommon exception url={}", url);
    		e.printStackTrace();
		}
		finally
		{
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
			long interval = System.currentTimeMillis() - start;
			LOGGER.debug("--------->HttpsClientCommon postData url={} elapsed time={} second", url, interval);
		}
    	return response;
    }
    
    public static String doGetUrl(String url, Map<String, String> headers)
    {
    	String response="";
    	HttpGet httpGet = new HttpGet(url);
    	long start = System.currentTimeMillis();
    	try
    	{
            //用get方法发送http请求   
    		if (headers != null && headers.size() > 0) {
                Set<Map.Entry<String, String>> set = headers.entrySet();
                for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
                    Map.Entry<String, String> header = it.next();
                    if (header != null) {
                    	httpGet.setHeader(header.getKey(), header.getValue());
                    }
                }
            }
    		System.out.println("--------->HttpsClientCommon get uri="+httpGet.getURI());
    		CloseableHttpResponse httpResponse =null;
        	HttpEntity entity=null;
        	if(httpClient!=null&&httpGet!=null){
        		httpResponse=httpClient.execute(httpGet);
        		entity= httpResponse.getEntity();
        	}
        	if (null != entity)
        	{
        		//response=EntityUtils.toString(entity);
        		response = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        		LOGGER.info("--------->HttpsClientCommon response code={}, content={}", httpResponse.getStatusLine(), response);
        	}
        	httpResponse.close();
           
    	} catch (Exception e) 
		{
    		LOGGER.error("--------->HttpsClientCommon exception url={}", url);
    		e.printStackTrace();
		}
		finally
		{
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
			long interval = System.currentTimeMillis() - start;
			LOGGER.debug("--------->HttpsClientCommon postData url={} elapsed time={} second", url, interval);
		}
    	return response;
    }
    
    public static String getByParams(String url, Map<String, String> map) throws Exception {

        if (StringUtils.isEmpty(url)) 
        {
            throw new RuntimeException("HTTP GET url is Empty");
        }
        String params = MapUrlUtil.getUrlParamsByMap(map, false);

        if(url.lastIndexOf("?")==-1)
    	    url += "?";
        url += params;
        
        HttpGet get = new HttpGet(url);
        HttpResponse response = httpClient.execute(get);
        int code = response.getStatusLine().getStatusCode();
        org.apache.http.HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, Consts.UTF_8);

    }
 
    /*
    public static int postJsonEx(String url, Map<String, Object> input, Map<String, Object> ouput) {
        HttpPost httpPost = new HttpPost(url);;
        int nRet = 1;
        try {
            // 设置请求头信息
            httpPost.setHeader("Content-Type", "application/json");
            // 将map转换成json然后发送
            String query = MyFastjsonUtil.mapToJson(input);
            httpPost.setEntity(new StringEntity(query, "utf-8"));
            
            HttpResponse response = httpClient.execute(httpPost);
       	 	int statusCode = response.getStatusLine().getStatusCode();
       	 	if(statusCode == HttpStatus.SC_OK)
       	 	{
       	 		String result = EntityUtils.toString(response.getEntity(), "utf-8");
       	 		ouput = MyFastjsonUtil.jsonToMap(result);
       	 		nRet = 0;
       	 	}            
        }
        catch (Exception e) {
        	nRet = -1;
        	LOGGER.error("--------->HttpsClientUtil exception url={}", url);
            e.printStackTrace();
        }
        finally {
            // 释放连接
        	 if (httpPost != null) {
             	httpPost.releaseConnection();
             }
        }
        return nRet;
    }
    */
    /*
    public static Map<String, Object> postJsonRetry(String url, Map<String, Object> data, int retrys, int sleepms){
        HttpPost httpPost = new HttpPost(url);
        Map<String, Object> resultmap = null;
        try {
            // 设置请求头信息
            httpPost.setHeader("Content-Type", "application/json");
            // 将map转换成json然后发送
            String query = MyFastjsonUtil.mapToJson(data);
            httpPost.setEntity(new StringEntity(query, "utf-8"));
            
            while(--retrys>0)
            {
            	 HttpResponse response = httpClient.execute(httpPost);
            	 int statusCode = response.getStatusLine().getStatusCode();
                 if(statusCode == HttpStatus.SC_OK)
                 {
                	 String result = EntityUtils.toString(response.getEntity(), "utf-8");
                	 resultmap = MyFastjsonUtil.jsonToMap(result);
                	 break;
                 }
                 Thread.sleep(sleepms);
            }
           
        }
        catch (Exception e) {
        	LOGGER.error("--------->HttpsClientUtil exception url={}", url);
        	e.printStackTrace();
        	resultmap = new HashMap<String, Object>();
        	resultmap.put("retcode", ConstantsCode.RETCODE_EXCEPTION);
        	resultmap.put("retmsg", ConstantsCode.globalMapErrorCode.get(ConstantsCode.RETCODE_EXCEPTION));
        }
        finally {
            // 释放连接
        	 if (httpPost != null) {
             	httpPost.releaseConnection();
             }
        }
        return resultmap;
    }
    */
}
