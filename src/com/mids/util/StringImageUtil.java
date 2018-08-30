package com.mids.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mids.ConstantsCode;


public class StringImageUtil {

	
    public static String StrToImg(String rootPath, String imageData,String imageFileName){

    	String imgDir ="";	
    	OutputStream out =null;
		try {
    		// Base64解码
    		byte[] b = Base64Util.decode(imageData);
    		for (int i = 0; i < b.length; ++i) {
    		if (b[i] < 0) {// 调整异常数据
    		b[i] += 256;
    		}
    		}
    		imgDir = rootPath;
    		//imgDir+=Constants.ICON_PATH+imgName;
    		imgDir+=imageFileName;
    		System.out.println("================"+imgDir+"==============");
    		out = new FileOutputStream(imgDir);
    		out.write(b);
    		out.flush();
    		out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally
        {
        	if(out!=null)
        	{
        		try
        		{
        			// 关闭文件
        			out.close();
        		}
        		catch(IOException e)
        		{
        			
        		}
        	}
        }
		return imageFileName;
    }
    
    public static void uploadImg(MultipartFile sourceFile,String realPath,String fileName){
    	try {
			FileUtils.copyInputStreamToFile(sourceFile.getInputStream(), new File(realPath, fileName));
//			sourceFile.transferTo(new File(realPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
