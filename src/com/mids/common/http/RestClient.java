package com.mids.common.http;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
 
import org.apache.http.ParseException;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
 
/**
 * @title：使用spring的restTemplate替代httpclient工具
 * @author：wncheng
 * @date：2015-05-18 08:48
 */
@Component
@Lazy(false)
public class RestClient {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);
    private static final int timeToLive = 30;//长连接保持30秒
    private static final int maxTotal = 3000;//同路由的并发数
    private static final int defaultMaxPerRoute = 1000;
    private static final int connectTimeout = 10000;//millesecond
    private static final int readTimeout = 10000;//millesecond
    private static final int connectionRequestTimeout = 200;//millesecond
    private static final int retrys = 3;
    
    private static final String https_flag = "on";
    private static final String keystore_flag = "off";
    private static final String https_keystore = "eaop.keystore";
    private static final String https_passwrod = "ydgj123icmcc";
    
    private static RestTemplate restTemplate;
 
    static {
    	HttpClientBuilder httpClientBuilder = HttpClients.custom();
    	// 长连接保持30秒
    	PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(timeToLive, TimeUnit.SECONDS);
    	// setup a Trust Strategy that allows all certificates.
    	
    	if(https_flag.equalsIgnoreCase("on"))
    	{
    		pollingConnectionManager = initSSLHttpsPool(httpClientBuilder);
    	}

        // 总连接数
        pollingConnectionManager.setMaxTotal(maxTotal);
        // 同路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        // 重试次数，默认是3次，没有开启
        //httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(retrys, true));
        httpClientBuilder.setRetryHandler(new RetryHandler(retrys));
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
 
		//RequestConfig.Builder builder = RequestConfig.custom();
		//builder.setConnectionRequestTimeout(200);
		//builder.setConnectTimeout(5000);
		//builder.setSocketTimeout(5000);
		//
		//RequestConfig requestConfig = builder.build();
		//httpClientBuilder.setDefaultRequestConfig(requestConfig);
 
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        headers.add(new BasicHeader("Accept-Language", "zh-CN"));
        headers.add(new BasicHeader("Connection", "Keep-Alive"));
 
        httpClientBuilder.setDefaultHeaders(headers);
 
        HttpClient httpClient = httpClientBuilder.build();
 
        // httpClient连接配置，底层是配置RequestConfig
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // 连接超时
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        // 数据读取超时时间，即SocketTimeout
        clientHttpRequestFactory.setReadTimeout(readTimeout);
        // 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
        clientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        // 缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。
        // clientHttpRequestFactory.setBufferRequestBody(false);
 
        // 添加内容转换器
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
 
        restTemplate = new RestTemplate(messageConverters);
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
 
        LOGGER.info("--------->RestClient init finish...");
    }
 
    private RestClient() {
 
    }
 
    @PostConstruct
    public static RestTemplate getClient() {
        return restTemplate;
    }
    
    
    private static PoolingHttpClientConnectionManager initSSLHttpsPool(HttpClientBuilder httpClientBuilder)
    {
    	PoolingHttpClientConnectionManager pollingConnectionManager= null;
    	try
		{
    		SSLContext sslContext = null;
    		if(keystore_flag.equalsIgnoreCase("on"))
    		{
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				    			
				String classpath = RestClient.class.getClassLoader().getResource("").getPath();    			
				FileInputStream instream = new FileInputStream(new File(classpath+https_keystore));
				// 加载keyStore d:\\tomcat.keystore
				try
				{
					trustStore.load(instream, https_passwrod.toCharArray());
				}catch (CertificateException e)
				{
					LOGGER.error("--------->RestClient init CertificateException...");
					e.printStackTrace();
				}
				finally
				{
					try
					{
						instream.close();
					} catch (Exception ignore) {}
				}
				sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
    		}
    		else
    		{
    			sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
    		}
			// don't check Hostnames, either.
	        // -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if
	        // you don't want to weaken
	        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

	        // here's the special part:
	        // -- need to create an SSL Socket Factory, to use our weakened "trust
	        // strategy";
	        // -- and create a Registry, to register it.
	        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
	        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
	                .register("http", PlainConnectionSocketFactory.getSocketFactory())
	                .register("https", sslSocketFactory).build();

	        // now, we create connection-manager using our Registry.
	        // -- allows multi-threaded use
	        pollingConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
	        
	    	httpClientBuilder.setSslcontext(sslContext);
        	httpClientBuilder.setSSLSocketFactory(sslSocketFactory);
        	
        } catch (ParseException e) {
        	LOGGER.error("--------->RestClient init ParseException...");
        	e.printStackTrace();
        } catch (FileNotFoundException e) {
        	LOGGER.error("--------->RestClient init FileNotFoundException...");
        	e.printStackTrace();
        }catch (IOException e) {
        	LOGGER.error("--------->RestClient init IOException...");
        	e.printStackTrace();
        } catch (KeyManagementException e) {
        	LOGGER.error("--------->RestClient init KeyManagementException...");
        	e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
        	LOGGER.error("--------->RestClient init NoSuchAlgorithmException...");
        	e.printStackTrace();
        } catch (KeyStoreException e) {
        	LOGGER.error("--------->RestClient init KeyStoreException...");
        	e.printStackTrace();
        }
    	return pollingConnectionManager;
    }
}