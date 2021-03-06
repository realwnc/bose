package com.mids.common.utils;

import java.io.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
//------------------------------------
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
//------------------------------------
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyFtpClient
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MyFtpClient.class);
	 
	protected String host;
	protected Integer port=21;
	protected String username;
	protected String password;
	protected String basepath;

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBasepath() {
		return basepath;
	}
	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}
	//private int reply;
	private FTPClient ftpClient = null;
	private boolean binaryTransfer = true;
	private boolean ftpStatus;
	
	public MyFtpClient(){
	}
	
	
	public boolean login(String host, int port, String username, String password, String basepath)
	{
		try
        {
			this.host = host;
			this.username = username;
			this.password = password;
			this.port = port;
			this.basepath = basepath;
			
			ftpClient = new FTPClient();
			ftpClient.setControlEncoding("UTF-8");
		
			ftpClient.connect(host, port);
			LOGGER.info("--------->MyFtpClient attemp to connecting to:" + host);
            int reply = ftpClient.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)==false)
            {
                ftpClient.disconnect();
                LOGGER.info("--------->FTP server refused connection.");
                return false;
            }
			try
        	{
		 		if (ftpClient.login(username, password)==false) 
				{
		 			ftpClient.logout();
		 			return false;
		 		}
         		if (binaryTransfer) 
				{
         			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
         		}
         		ftpClient.enterLocalPassiveMode();
			}
			catch (IOException e)
        	{
				e.printStackTrace();
				return false;
        	}
			LOGGER.info("--------->Succeed to connecting to:" + host);
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        	return false;
        }		 
	   return true;
	}
	public boolean logout()
	{
		boolean ret;
		try
		{
			ret = ftpClient.logout();
			ftpClient.disconnect();
		}
		catch (IOException e)
        {
        	e.printStackTrace();
        	ret = false;
        }	
		return ret;
	}

	//ftpPath =mchtCode+"/"+Constants.FILE_STORE_PATH_VIDEOCOVER
	public boolean uploadFile(String ftpPath, File myfile)
	{
		InputStream input=null;
		try
		{
			LOGGER.info("--------->FTP work path={}",ftpClient.printWorkingDirectory());
			String filename=myfile.getName();
			//String fullfilename = myfile.getPath();
			//int pos = fullfilename.lastIndexOf("/");
			//String fileame =filepath.substring(pos+1);
			
			ftpClient.changeWorkingDirectory(basepath);
			String[] path = ftpPath.split("/");
			for(String str : path)
			{
				if(StringUtils.isBlank(str) == true)
				{
					continue;
				}
				if(ftpClient.changeWorkingDirectory(str)==false)
				{      
					ftpStatus = ftpClient.makeDirectory(str);
					if(ftpStatus == true)
					{
						ftpStatus = ftpClient.changeWorkingDirectory(str);
						if(ftpStatus == false)
						{
							return false;
						}
					}
					else
					{
						return false;
					}
				}
			}
	       
	        input = new FileInputStream(myfile); 
	        ftpStatus=ftpClient.storeFile(ftpClient.printWorkingDirectory()+"/"+filename, input);        
	        input.close();
	        return ftpStatus;
		}
		catch(IOException e)
		{
			LOGGER.error("--------->Exception={}", e);
		}
		finally
		{
			if(input!=null)
			{
				try
				{
					input.close();
				}
				catch(IOException e)
				{
					LOGGER.error("--------->Exception={}", e);
				}
			}
		}
 		return false;
	}
	//ftpPath =mchtCode+"/"+Constants.FILE_STORE_PATH_VIDEOCOVER
	public boolean uploadFile(String ftpPath, String localFilenamePath)
	{
		File myfile = new File(localFilenamePath);
		if(myfile.exists() == false)
		{
			return false;
		}
		return uploadFile(ftpPath, myfile);
	}
	
	/*
	 * ftpFilename=/gzecsr/media/1.avi
	 * localFilenamePath=d:\\gzecsr\\media\\2.avi
	 */
	public boolean download(String ftpFilename, String localFilenamePath)
	{
		OutputStream output = null;
		String remoteFilename = ftpFilename;
		try{
			//String remote=this.p.getProperty("FTP_BasePath")+"/"+filename;
			ftpClient.changeWorkingDirectory(basepath);
			localFilenamePath = localFilenamePath.replaceAll("\\\\", "/");
			int pos = localFilenamePath.lastIndexOf("/");
			String localPath = localFilenamePath.substring(0, pos);
			//String newFilename = localFilenamePath.substring(pos+1);
			File dir=new File(localPath);
			if(dir.exists() == false)
			{
				dir.mkdirs();
			}
			File fileLocal=new File(localFilenamePath);
			
	        output = new FileOutputStream(fileLocal);
	        
	        boolean bRet = ftpClient.retrieveFile(remoteFilename, output);
	        output.close();
	        if(bRet==false)
	        {
	        	LOGGER.error("--------->FTP failed download file[{}], to local file[{}]", remoteFilename, localFilenamePath);
	        }
	        return bRet;
		}
		catch(IOException e)
		{
			LOGGER.error("--------->Exception={}", e);
		}
		finally
		{
			if(output!=null)
			{
				try
				{
					output.close();
				}
				catch(IOException e)
				{
					LOGGER.error("--------->Exception={}", e);
				}
			}
		}
 		return false;
	}
	
	public boolean download(String ftpFilename, File file)
	{
		OutputStream output = null;
		String remoteFilename = ftpFilename;
		try{
			//String remote=this.p.getProperty("FTP_BasePath")+"/"+filename;
			ftpClient.changeWorkingDirectory(basepath);
			//String newFilename = localFilenamePath.substring(pos+1);
			
	        output = new FileOutputStream(file);
	        
	        boolean bRet = ftpClient.retrieveFile(remoteFilename, output);
	        output.close();
	        if(bRet==false)
	        {
	        	LOGGER.error("--------->FTP failed download file[{}], to local file[{}]", remoteFilename, file.getAbsolutePath());
	        }
	        return bRet;
		}
		catch(IOException e)
		{
			LOGGER.error("--------->Exception={}", e);
		}
		finally
		{
			if(output!=null)
			{
				try
				{
					output.close();
				}
				catch(IOException e)
				{
					LOGGER.error("--------->Exception={}", e);
				}
			}
		}
 		return false;
	}
	
	//ftpFilename=/gzecsr/media/1.avi
	public boolean removeFile(String ftpFilename) throws Exception
	{			
		String remote=basepath+"/"+ftpFilename;	
        return ftpClient.deleteFile(remote);
	}
	
	//fileFrom=/gzecsr/media/1.avi
	//fileTo=/gzecsr/media/2.avi
	public boolean renameFile(String fileFrom, String fileTo) throws Exception
	{			
		String from=basepath+"/"+fileFrom;	
		String to=basepath+"/"+fileTo;
        return ftpClient.rename(from,to);
	}
	
	public List<String> getFileList(String path)
	{
		List<String> filelist=new ArrayList<String>();
		//System.out.println("wwwwwwwwwppppppppwwwwwwww");
		try
        {
			ftpClient.changeWorkingDirectory(basepath);
			String[] dirs = path.split("/");
	        for(String str : dirs)
			{
				if(StringUtils.isBlank(str) == false)
				{
					if(ftpClient.changeWorkingDirectory(str) == false)
					{
						LOGGER.error("--------->FTP failed enter path={}, remotePath={}", str, path);
						return filelist;
					}
				}
			}
			
			String [] ftplist=ftpClient.listNames();
			for(int i=0;i<ftplist.length;i++)
			{
				filelist.add(path+"/"+ftplist[i]);
			}
        }
        catch (IOException e)
        	{e.printStackTrace();}
				
		return filelist;
	}
	

	//---------------------------------------------
	//ftpFilename=/gzecsr/media/1.avi
    public InputStream retrieveFileStream(/*String path, */String ftpFilename)
	{
    	InputStream in = null;
    	try{
    		/*
    		String[] dirs=path.split("/");
	        for(int i=0;i<dirs.length-1;i++)
			{
				if(StringUtils.isBlank(dirs[i]) == false)
				{
					ftpClient.changeWorkingDirectory(dirs[i]);
				}
			}
			*/
			in = ftpClient.retrieveFileStream(ftpFilename);
			ftpClient.getReply();
    	} catch (FileNotFoundException e) {  
    		LOGGER.warn("--------->FTP filename[{}] not found", ftpFilename);
    		e.printStackTrace();  
            return null;
        } catch (IOException e) {
        	LOGGER.warn("--------->FTP filename[{}] read error", ftpFilename);
            e.printStackTrace();  
            return null;
        } 
        return in;
	}
    
    //fileFrom=/gzecsr/media/1.avi
  	//fileTo=/gzecsr/media/2.avi
    public boolean copyFile(String srcFile, String destFile)
	{
    	InputStream input = null;
    	try{
    		String from=basepath+"/"+srcFile;	
    		String to=basepath+"/"+destFile;
    		input = ftpClient.retrieveFileStream(from);
    		ftpClient.getReply();
			ftpStatus=ftpClient.storeFile(to, input);        
	        input.close();
	        return ftpStatus;
	        
    	} catch (FileNotFoundException e) {  
    		LOGGER.warn("--------->FTP filename[{}] not found", srcFile);
    		e.printStackTrace();  
            return false;
        } catch (IOException e) {
        	LOGGER.warn("--------->FTP filename[{}] read error", srcFile);
            e.printStackTrace();  
            return false;
        }
	}
    //ftpPath=/gzecsr/media
    public boolean removePath(String ftpPath) throws Exception
	{			
		String path=basepath+"/"+ftpPath;	
		return removeDirectory(path);
	}
    private boolean removeDirectory(String path) throws Exception
	{			
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);  
        if (ftpFileArr == null || ftpFileArr.length == 0) {  
            return ftpClient.removeDirectory(path);//RemoveDirectory(path);  
        }  
  
        for (int i = 0; i < ftpFileArr.length; i++) {  
            FTPFile ftpFile = ftpFileArr[i];  
            String name = ftpFile.getName();  
            if (ftpFile.isDirectory()) {  
            	removeDirectory(path + "/" + name);  
            } else if (ftpFile.isFile()) {  
            	ftpClient.deleteFile(path + "/" + name);  
            } else if (ftpFile.isSymbolicLink()) {  
                  
            } else if (ftpFile.isUnknown()) {  
                  
            }  
        }  
        return ftpClient.removeDirectory(path);  
	}
}


