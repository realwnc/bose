package com.mids.common.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 读取properties文件
 * 
 */
public class SystemPropertiesUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemPropertiesUtil.class);
	
    private static Properties propertie;
    private static SystemPropertiesUtil instance = null;
    private static Integer lock = new Integer(1);
    public static String FILE_PATH = "/system.properties";

    public static SystemPropertiesUtil getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new SystemPropertiesUtil(FILE_PATH);
                }
            }
        }
        return instance;
    }

    /**
     * 初始化Configuration类
     * 
     * @param filePath
     *            要读取的配置文件的路径+名称
     */
    private SystemPropertiesUtil(String filePath) {
        propertie = new Properties();
        InputStream inputFile = null;
        try {

            inputFile = getClass().getResourceAsStream(filePath);
            propertie.load(inputFile);

        }
        catch (FileNotFoundException ex) {
        	LOGGER.error("--------->error on open properties file, FileNotFoundException, file="+filePath, ex);
            // ex.printStackTrace();
        }
        catch (IOException ex) {
        	LOGGER.error("--------->error on read properties file!!!", ex);
            // ex.printStackTrace();
        }
        finally {
            try {
                if (inputFile != null) {
                    inputFile.close();
                }
            }
            catch (Exception e) {
            	LOGGER.error("---------->error on close string of properties, Exception file=" + filePath, e);
            }
        }
    }// end ReadConfigInfo(...)

    /**
     * 重载函数，得到key的值
     * 
     * @param key
     *            取得其值的键
     * @return key的值
     */
    public String getValue(String key) {
        if (propertie.containsKey(key)) {
            String value = propertie.getProperty(key);// 得到某一属性的值
            return value;
        }
        else return "";
    }

    /**
     * 重载函数，得到key的值
     * 
     * @param fileName
     *            properties文件的路径+文件名
     * @param key
     *            取得其值的键
     * @return key的值
     */
    public String getValue(String fileName, String key) {
        InputStream inputFile = null;
        try {
            String value = "";

            inputFile = getClass().getResourceAsStream(fileName);
            propertie.load(inputFile);
            if (propertie.containsKey(key)) {
                value = propertie.getProperty(key);
                return value;
            }
            else return value;
        }
        catch (FileNotFoundException e) {
        	LOGGER.error("---------->error on getValue FileNotFoundException file="+fileName, e);
            // e.printStackTrace();
            return "";
        }
        catch (IOException e) {
            LOGGER.error("---------->error on getValue IOException file="+fileName, e);
            // e.printStackTrace();
            return "";
        }
        catch (Exception ex) {
        	LOGGER.error("---------->error on getValue Exception file="+fileName, ex);
            // ex.printStackTrace();
            return "";
        }
        finally {
            try {
                if (inputFile != null) {
                    inputFile.close();
                }
            }
            catch (Exception e) {
            	LOGGER.error("--------->error on close stream Exception file="+fileName, e);
            }
        }
    }// end getValue(...)

    /**
     * 清除properties文件中所有的key和其值
     */
    public void clear() {
        propertie.clear();
    }

    /**
     * 改变或添加一个key的值，当key存在于properties文件中时该key的值被value所代替， 当key不存在时，该key的值是value
     * 
     * @param key
     *            要存入的键
     * @param value
     *            要存入的值
     */
    public void setValue(String key, String value) {
        propertie.setProperty(key, value);
    }

    /**
     * 将更改后的文件数据存入指定的文件中，该文件可以事先不存在。
     * 
     * @param fileName
     *            文件路径+文件名称
     * @param description
     *            对该文件的描述
     */
    public void saveFile(String fileName, String description) {
        String filePath = getClass().getResource("/").getPath() + fileName;
        File file = new File(filePath);
        FileOutputStream outputFile = null;
        try {
            outputFile = new FileOutputStream(file);
            propertie.store(outputFile, description);

        }
        catch (FileNotFoundException e) {
        	LOGGER.error("---------->error on save file, FileNotFoundException file="+fileName, e);
            // e.printStackTrace();
        }
        catch (IOException ioe) {
        	LOGGER.error("---------->error on save file, IOException file"+fileName, ioe);
            // ioe.printStackTrace();
        }
        finally {
            try {
                if (outputFile != null) {
                    outputFile.close();
                }
            }
            catch (Exception e) {
            	LOGGER.error("--------->error on close stream Exception file="+fileName, e);
            }
        }
    }

}