package com.mids.service.impl;

import com.mids.common.Result;
import com.mids.common.utils.SysAssistUtil;
import com.mids.service.UploadFileService;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    private static Logger LOGGER = LoggerFactory.getLogger(UploadFileServiceImpl.class);
    
	@Override
	public Result singleUploadFile(String path, MultipartFile file)
    {
    	Result result = new Result();
    	
    	//String realPath = request.getSession().getServletContext().getRealPath(Constants.CRMRES_PATH_MCHTROOT);
    	//String targetFilepath = realPath + "/" + mchtCode + "/" + Constants.FILE_STORE_PATH_USERVOUCHER;
    	String targetFilepath = path;
    	File filedir = new File(targetFilepath);
    	if(filedir.exists() == false)
    	{
    		filedir.mkdirs();
    	}
    	String orgFilename = file.getOriginalFilename();
    	LOGGER.debug("--------->processSwfUploadFile org filename={}", orgFilename);
        if(SysAssistUtil.checkImportFileSupport(orgFilename) == false)
        {
        	result.setMsg("文件格式不支持");
        	return result;
        }
        
        
        String destFilename = orgFilename;
        destFilename = destFilename.replaceAll("'", ""); //去掉'字符
        destFilename = destFilename.replaceAll("%", "_"); //%->_                
        
        File fileOld = new File(targetFilepath, destFilename);
        if(fileOld.exists() == true)
        {
        	fileOld.delete();
        }
        try {
        	FileUtils.copyInputStreamToFile(file.getInputStream(), fileOld);
		} catch (IOException e) {
			LOGGER.warn("--------->failed to copyInputStreamToFile, path={}, filiename={}", path, fileOld);
			e.printStackTrace();
        	result.setMsg("上传失败");
			return result;
		}
        
        File newFile = new File(targetFilepath, destFilename);
        
        if(newFile.exists() == false)
        {
        	LOGGER.warn("--------->failed not exist while uploading, path={}, filiename={}", path, orgFilename);
        	result.setMsg("上传失败");
        	return result;
        }            
      
        result.setMsg("上传成功");
    	result.setSuccess(true);
    	result.setObj(newFile.getAbsolutePath());
    	return result;
    }
}
