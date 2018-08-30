package com.mids.sysassist;

import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.mids.util.crypt.MyAESUtils;

public class MyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyPropertyPlaceholderConfigurer.class);
	private static final String KEY_ENCRYPT_SUFFIX = ".encrypt";
	
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException
    {
        Enumeration<?> keys = props.propertyNames();
        while (keys.hasMoreElements())
        {
            String key = (String)keys.nextElement();
            String value = props.getProperty(key);
            if (key.endsWith(KEY_ENCRYPT_SUFFIX) && null != value)
            {
            	try
                {
            		props.remove(key);  
                    key = key.substring(0, key.length() - KEY_ENCRYPT_SUFFIX.length());
                	value = MyAESUtils.aesDecrypt(value.trim());
                }
                catch(Exception e)
                {
                	e.printStackTrace();
                	LOGGER.error("--------->MyPropertyPlaceholderConfigurer exception={}", e);
                }
                props.setProperty(key, value);

                LOGGER.debug("--------->processProperties key={}, value={}", key, value);
            }

            System.setProperty(key, value);
        }
        
        super.processProperties(beanFactoryToProcess, props);
    }
}