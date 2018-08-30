package com.mids.common.http;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;  
import org.springframework.web.client.HttpClientErrorException;

import com.mids.util.MyFastjsonUtil;
import com.mids.util.MapUrlUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpClientUtils {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);
    
    public static String doPostJson(String url, Map<String, String> headermap, String jsonstr) {      
        long start = System.currentTimeMillis();
        String result = "";
        try {
        	HttpHeaders headers = new HttpHeaders();
        	MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        	headers.setContentType(type);
        	headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        	if(headermap != null)
        	{
	        	for (String key : headermap.keySet()) {
	        		//System.out.println("key= "+ key + " and value= " + headermap.get(key));
	        		headers.add(key, headermap.get(key).toString());
	        	}
	        }
        	
        	String query = jsonstr;
        	String printlog = query;
            if(StringUtils.isBlank(printlog) == false)
            {
            	printlog=printlog.length()>64?printlog.substring(0,64):printlog;
            }
        	LOGGER.info("--------->HttpClientUtils request body={}", printlog);
        	
        	HttpEntity<String> requestEntity = new HttpEntity<String>(query, headers);
        	
        	//Map<String, Object> respmap = RestClient.getClient().postForObject(url, requestEntity, Map.class);
        	ResponseEntity<String> responseEntity = RestClient.getClient().postForEntity(url, requestEntity, String.class);
        	MediaType contentType = responseEntity.getHeaders().getContentType();
        	HttpStatus statusCode = responseEntity.getStatusCode();
        	
        	result = responseEntity.getBody();
        	
        }
        catch (HttpClientErrorException e)
        {
        	LOGGER.error("--------->HttpClientUtils POST request error={}", url, e);        	
         	LOGGER.error("--------->HttpClientUtils errorCode={}, statusText={}, responseBody={}, message={}" + e.getStatusCode(), e.getStatusText(), e.getResponseBodyAsString(), e.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("--------->HttpClientUtils POST request error={}", url, e);
            LOGGER.error("--------->HttpClientUtils POST request ResponseBody={}", e.getMessage());
        }
        finally {
        	long interval = System.currentTimeMillis() - start;
            LOGGER.debug("--------->HttpsClientUtil postData url={} elapsed time={} second", url, interval);
        }
 
        return result;
    }
    public static  String doPostJson(String url, Map<String, String> headermap, Map<String, Object> requestmap) {
    	String query = MyFastjsonUtil.map2Json(requestmap);
    	String result = doPostJson(url, headermap, query);
 
        return result;
    }
    
    /**
     * post请求
     * @param url
     * @param formParams
     * @return
     */
    public static String doPostForm(String url, Map<String, String> formParams/*, Map<String, String> headers*/) {
        if (MapUtils.isEmpty(formParams)) {
            return doPost(url);
        }
        String result = "";
        try {
            MultiValueMap<String, Object> requestEntity = new LinkedMultiValueMap<>();
            
            Set<Map.Entry<String, String>> set = formParams.entrySet();
            for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                if (entry != null) {
                	requestEntity.add(entry.getKey(), entry.getValue());
                }
            }
            //result = RestClient.getClient().postForObject(url, requestEntity, String.class);
            ResponseEntity<String> responseEntity = RestClient.getClient().postForEntity(url, requestEntity, String.class);
        	MediaType contentType = responseEntity.getHeaders().getContentType();
        	HttpStatus statusCode = responseEntity.getStatusCode();
        	
        	result = responseEntity.getBody();
            //formParams.keySet().stream().forEach(key->requestEntity.add(key, MapUtils.getString(formParams, key, "")));
            //return RestClient.getClient().postForObject(url, requestEntity, String.class);
        }
        catch (HttpClientErrorException e)
        {
        	LOGGER.error("--------->HttpClientUtils POST request error={}", url, e);
         	//ObjectMapper mapper = new ObjectMapper();
         	//ErrorHolder eh = mapper.readValue(e.getResponseBodyAsString(), ErrorHolder.class);
         	//LOGGER.error("error code:  " + eh.getStatusCode());
         	LOGGER.error("--------->HttpClientUtils POST request code={}", e.getStatusCode());
        }
        catch (Exception e) {
            LOGGER.error("--------->HttpClientUtils POST request error={}", url, e);
        }
 
        return result;
    }
 
    /**
     * post请求
     * @param url
     * @param formParams
     * @return
     */
    public static String doPostForm(String url,  Map<String, String> headermap, Map<String, String> formmap) {
        if (MapUtils.isEmpty(formmap)) {
            return doPost(url);
        }
        String result = "";
        try {
        	HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        	if(headermap != null)
        	{
	        	for (String key : headermap.keySet()) {
	        		headers.add(key, headermap.get(key).toString());
	        	}
	        }
            MultiValueMap<String, String> formdata = new LinkedMultiValueMap<>();
            for (String key : formmap.keySet()) {
            	formdata.add(key, formmap.get(key).toString());
        	}
            /*
            Set<Map.Entry<String, String>> set = formParams.entrySet();
            for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                if (entry != null) {
                	formmap.add(entry.getKey(), entry.getValue());
                }
            }
            */
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(formdata, headers);
  
            //ResponseEntity<String> responseEntity = RestClient.getClient().postForEntity(url, request, String.class);
            ResponseEntity<String> responseEntity = RestClient.getClient().exchange(url, HttpMethod.POST, requestEntity, String.class);
        	MediaType contentType = responseEntity.getHeaders().getContentType();
        	HttpStatus statusCode = responseEntity.getStatusCode();
        	
        	result = responseEntity.getBody();
            //formParams.keySet().stream().forEach(key->requestEntity.add(key, MapUtils.getString(formParams, key, "")));
            //return RestClient.getClient().postForObject(url, requestEntity, String.class);
        }
        catch (HttpClientErrorException e)
        {
        	LOGGER.error("--------->HttpClientUtils POST request error={}", url, e);
         	//ObjectMapper mapper = new ObjectMapper();
         	//ErrorHolder eh = mapper.readValue(e.getResponseBodyAsString(), ErrorHolder.class);
         	//LOGGER.error("error code:  " + eh.getStatusCode());
         	LOGGER.error("--------->HttpClientUtils POST request code={}", e.getStatusCode());
        }
        catch (Exception e) {
            LOGGER.error("--------->HttpClientUtils POST request error={}", url, e);
        }
 
        return result;
    }
    /**
     * post请求
     * @param url
     * @return
     */
    public static String doPost(String url) {
    	String result = "";
        try {        	
        	//return RestClient.getClient().postForObject(url, HttpEntity.EMPTY, String.class);
        	
        	ResponseEntity<String> responseEntity = RestClient.getClient().postForEntity(url, HttpEntity.EMPTY, String.class);
        	MediaType contentType = responseEntity.getHeaders().getContentType();
        	HttpStatus statusCode = responseEntity.getStatusCode();
        	
        	result = responseEntity.getBody();
        }
        catch (HttpClientErrorException e)
        {
        	LOGGER.error("--------->HttpClientUtils POST request error={}", url, e);
         	//ObjectMapper mapper = new ObjectMapper();
         	//ErrorHolder eh = mapper.readValue(e.getResponseBodyAsString(), ErrorHolder.class);
         	//LOGGER.error("error code:  " + eh.getStatusCode());
         	LOGGER.error("--------->HttpClientUtils POST request code={}", e.getStatusCode());
        }
        catch (Exception e) {
            LOGGER.error("POST请求出错：{}", url, e);
        }
 
        return result;
    }

 
    /**
     * get请求
     * @param url
     * @return
     */
    public static String doGet(String url) {
    	String result = "";
        try {
            //return RestClient.getClient().getForObject(url, String.class);

        	ResponseEntity<String> responseEntity = RestClient.getClient().getForEntity(url, String.class);
        	MediaType contentType = responseEntity.getHeaders().getContentType();
        	HttpStatus statusCode = responseEntity.getStatusCode();
        	result = responseEntity.getBody();
        }
        catch (HttpClientErrorException e)
        {
        	LOGGER.error("--------->HttpClientUtils GET request error url={}", url, e);
         	//ObjectMapper mapper = new ObjectMapper();
         	//ErrorHolder eh = mapper.readValue(e.getResponseBodyAsString(), ErrorHolder.class);
         	//LOGGER.error("error code:  " + eh.getStatusCode());
         	LOGGER.error("error code:  " + e.getStatusCode());
        }
        catch (Exception e) {
            LOGGER.error("--------->HttpClientUtils GET request error url={}", url, e);
        }
 
        return result;
    }
    
    /**
     * get请求
     * @param url
     * @return
     */
    public static String doGet(String url, Map<String, String> map) {
    	String result = "";
        try {
            //return RestClient.getClient().getForObject(url, String.class);
        	String params = MapUrlUtil.getUrlParamsByMap(map, false);
            if(url.lastIndexOf("?")==-1)
        	    url += "?";
            url += params;
        	ResponseEntity<String> responseEntity = RestClient.getClient().getForEntity(url,String.class);
        	
        	MediaType contentType = responseEntity.getHeaders().getContentType();
        	HttpStatus statusCode = responseEntity.getStatusCode();
      
        	result = responseEntity.getBody();
        }
        catch (HttpClientErrorException e)
        {
        	LOGGER.error("--------->HttpClientUtils GET request error url={}", url, e);
         	//ObjectMapper mapper = new ObjectMapper();
         	//ErrorHolder eh = mapper.readValue(e.getResponseBodyAsString(), ErrorHolder.class);
         	//LOGGER.error("error code:  " + eh.getStatusCode());
         	LOGGER.error("error code:  " + e.getStatusCode());
        }
        catch (Exception e) {
            LOGGER.error("--------->HttpClientUtils GET request error url={}", url, e);
        }
 
        return result;
    }

    /**
     * delete 请求
     * @param url
     * @return
     */
    public static int doDelete(String url, Map<String, Object> headermap ) {
    	int nRet = 0;
        try {
        	HttpHeaders headers = new HttpHeaders();
        	//MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        	//headers.setContentType(type);
        	//headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        	for (String key : headermap.keySet()) {
        		//System.out.println("key= "+ key + " and value= " + headermap.get(key));
        		headers.add(key, headermap.get(key).toString());
        	}
        	HttpEntity<byte[]> requestEntity = new HttpEntity<byte[]>(headers);
        	ResponseEntity<byte[]> responseEntity = RestClient.getClient().exchange(url, HttpMethod.DELETE, requestEntity, byte[].class);
        	HttpStatus stautsCode = responseEntity.getStatusCode();
        	nRet = stautsCode.value();
        	if(stautsCode==HttpStatus.OK)
        	{
        		//succeed
        		nRet = 0;
        		LOGGER.info("--------->HttpClientUtils delete method succeed");
        		String response = responseEntity.getBody().toString();
        		LOGGER.info("--------->HttpClientUtils statuscode={}, response={}", stautsCode, response);
        	}
        	LOGGER.info("--------->HttpClientUtils statuscode={}", stautsCode);
            //RestClient.getClient().delete(url);
        }
        catch (HttpClientErrorException e)
        {
        	nRet = -1;
        	LOGGER.error("--------->HttpClientUtils delete error url={}", url, e);
         	//ObjectMapper mapper = new ObjectMapper();
         	//ErrorHolder eh = mapper.readValue(e.getResponseBodyAsString(), ErrorHolder.class);
         	//LOGGER.error("error code:  " + eh.getStatusCode());
         	LOGGER.error("--------->HttpClientUtils error code={}", e.getStatusCode());         	
        }
        catch (Exception e) {
        	nRet = -1;
            LOGGER.error("--------->HttpClientUtils delete error, url={}", url, e);
        }
 
        return nRet;
    }
    

    public static String doPutXmlContent(String url, Map<String, String> headermap, String xmlstr) {
    	String result = "";
    	long start = System.currentTimeMillis();
        try {
        	HttpHeaders headers = new HttpHeaders();
        	if(headermap != null)
        	{
	        	for (String key : headermap.keySet()) {
	        		//System.out.println("key= "+ key + " and value= " + headermap.get(key));
	        		headers.add(key, headermap.get(key).toString());
	        	}
	        }
        	String query = xmlstr;
        	String printlog = query;
            if(StringUtils.isBlank(printlog) == false)
            {
            	printlog=printlog.length()>64?printlog.substring(0,64):printlog;
            }
        	LOGGER.info("--------->HttpClientUtils request body={}", printlog);

        	HttpEntity<String> requestEntity = new HttpEntity<String>(query, headers);

        	ResponseEntity<String> responseEntity = RestClient.getClient().exchange(url, HttpMethod.PUT, requestEntity, String.class);
        	//MediaType contentType = responseEntity.getHeaders().getContentType();
        	//HttpStatus statusCode = responseEntity.getStatusCode();
        	
        	result = responseEntity.getBody();
        }
        catch (HttpClientErrorException e)
        {
        	LOGGER.error("--------->HttpClientUtils PUT request error={}", url, e);        	
         	LOGGER.error("--------->HttpClientUtils error code={}", e.getStatusCode());
        }
        catch (Exception e) {
            LOGGER.error("--------->HttpClientUtils POST request error={}", url, e);            
        }
        finally {
        	long interval = System.currentTimeMillis() - start;
            LOGGER.debug("--------->HttpsClientUtil doPutXmlContent url={} elapsed time={} second", url, interval);
        }
 
        return result;
    }
    
}