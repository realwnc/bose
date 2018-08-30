package com.mids.service;

import org.springframework.web.multipart.MultipartFile;

import com.mids.common.Result;

/**
 * @description：用户管理
 * @author：wncheng
 * @date：2015/10/1 14:51
 */
public interface UploadFileService {
    
    Result singleUploadFile(String path, MultipartFile file);
}
