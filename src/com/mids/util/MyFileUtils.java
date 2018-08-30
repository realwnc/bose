package com.mids.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Title: 天乙软件工作室公共包</p>
 * <p>Description: 天乙软件工作室公共包</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 天乙软件工作室[LAOER.COM/TIANYISOFT.NET]</p>
 * @author 龚天乙(Laoer)
 * @version 1.0
 */

public class MyFileUtils 
{
	private static final Log logger = LogFactory.getLog(MyFileUtils.class);
	public MyFileUtils(){}
	/**
	 * 删除文件夹里面的所有文件
	 * @param path String 文件夹路径 如 c:/fqf
	 */
	public static List<String> listFileOrDir(String path, boolean bDir) 
	{
		List<String> list = new ArrayList<String>();
		
		File file = new File(path);
		if (!file.exists()) 
		{
			return list;
		}
		if (!file.isDirectory()) 
		{
			return list;
		}
		String filepath;
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) 
		{
			if (path.endsWith(File.separator)) 
			{
				filepath = path + tempList[i];
				temp = new File(filepath);
			}
			else 
			{
				filepath = path + File.separator + tempList[i];
				temp = new File(filepath);
			}
			if (temp.isFile() && bDir == false) 
			{
				list.add(tempList[i]);
			}
			if (temp.isDirectory() && bDir == true)
			{
				list.add(tempList[i]);
			}
		}
		return list;
	}

	public static String readFile(String filePath) 
	{
		String info = "";
		try 
		{
			File f = new File(filePath);
			if (f.exists()) 
			{
				FileInputStream bw = new FileInputStream(f);
				int len = bw.available();
				byte[] str = new byte[len];
				if (bw.read(str) == -1) 
				{
					info = "";
				}
				else
				{
					info = new String(str);
				}
				bw.close();
				bw = null;
			}
			f = null;
		}
		catch (IOException e) 
		{
			logger.error(e);
		}
		return info;
	}

	public static String readFile(String filePath, String charset) 
	{
		String info = "";
		try 
		{
			File f = new File(filePath);
			if (f.exists()) 
			{
				FileInputStream bw = new FileInputStream(f);
				int len = bw.available();
				byte[] str = new byte[len];
				if (bw.read(str) == -1) 
				{
					info = "";
				}
				else 
				{
					info = new String(str, charset);
				}
				bw.close();
				bw = null;
			}
			f = null;
		}
		catch (IOException e) 
		{
			logger.error(e);
		}
		return info;
	}

	public static String readFile(File f) 
    {
        String info = "";
        try
        {
            if (f.exists()) 
            {
                FileInputStream bw = new FileInputStream(f);
                int len = bw.available();
                byte[] str = new byte[len];
                if (bw.read(str) == -1) 
                {
                    info = "";
                }
                else
                {
                    info = new String(str);
                }
                bw.close();
                bw = null;
            }
            f = null;
        }
        catch (IOException e) 
        {
            logger.error(e);
        }
        return info;
    }
 
    public static String readFile(File f, String charset) 
    {
        String info = "";
        try
        {
            if (f.exists()) 
            {
                FileInputStream bw = new FileInputStream(f);
                int len = bw.available();
                byte[] str = new byte[len];
                if (bw.read(str) == -1) 
                {
                    info = "";
                }
                else
                {
                    info = new String(str, charset);
                }
                bw.close();
                bw = null;
            }
            f = null;
        }
        catch (IOException e) 
        {
            logger.error(e);
        }
        return info;
    }
    
	public static int writeFile(String msg, String filePath) 
	{
		int ret = 0;
		try 
		{
			File file = new File(filePath);
			if (file.exists()) 
			{
				file.delete();
			}
			FileOutputStream wf = new FileOutputStream(filePath);
			wf.write(msg.getBytes());
			wf.close();
			file = null;
			wf = null;
		}
		catch (IOException e)
		{
			logger.error(e);
			ret = -1;
		}
		return ret;
	}

	public static int writeFile(String msg, String filePath, String charset) 
	{
		int ret = 0;
		try
		{
			int pos = filePath.lastIndexOf("/");
			if(pos < 0)
			{
				pos = filePath.lastIndexOf("\\");
			}
			if(pos > 0)
			{
				String path = filePath.substring(0, pos);
				File fpath = new File(path);
				if(fpath.exists() == false)
				{
					fpath.mkdirs();
				}
			}
			File file = new File(filePath);
			if (file.exists())
			{
				file.delete();
			}
			FileOutputStream wf = new FileOutputStream(filePath);
			wf.write(msg.getBytes(charset));
			wf.close();
			file = null;
			wf = null;
		}
		catch (IOException e) 
		{
			logger.error(e);
			ret = -1;
		}
		return ret;
	}
	
	public static int writeFile(String filePath, byte[] bbyte) 
    {
        int ret = 0;
        try
        {
            File file = new File(filePath);
            if (file.exists()) 
            {
                file.delete();
            }
            FileOutputStream wf = new FileOutputStream(filePath);
            wf.write(bbyte);
            wf.close();
            file = null;
            wf = null;
        }
        catch (IOException e)
        {
            logger.error(e);
            ret = -1;
        }
        return ret;
    }

	////
	/**
	 * 新建目录
	 * @param folderPath String 如 c:/fqf
	 * @return boolean
	 */
	public static void createFolder(String folderPath) 
	{
		try 
		{
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			if (!myFilePath.exists()) 
			{
				myFilePath.mkdirs();
			}
		}
		catch (Exception e)
		{
			System.out.println("mkdir error!!!");
			e.printStackTrace();
		}
	}

	/**
	 * 新建文件
	 * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent String 文件内容
	 * @return boolean
   	*/
	public static void newFile(String filePathAndName, String fileContent) 
	{
		try 
		{
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) 
			{
				myFilePath.createNewFile();
			}
			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);
			String strContent = fileContent;
			myFile.println(strContent);
			resultFile.close();
		}
		catch (Exception e) 
		{
			System.out.println("create file error!!!");
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent String
	 * @return boolean
	 */
	public static void delFile(String filePathAndName) 
	{
		try 
		{
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			myDelFile.delete();
		}
		catch (Exception e) 
		{
			System.out.println("delete file error");
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹
	 * @param filePathAndName String 文件夹路径及名称 如c:/fqf
	 * @param fileContent String
	 * @return boolean
	 */
	public static void delFolder(String folderPath) 
	{
		try
		{
			delAllFile(folderPath); //删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); //删除空文件夹
		}
		catch (Exception e) 
		{
			System.out.println("delete folder error!!!");
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * @param path String 文件夹路径 如 c:/fqf
	 */
	public static void delAllFile(String path) 
	{
		File file = new File(path);
		if (!file.exists()) 
		{
			return;
		}
		if (!file.isDirectory()) 
		{
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) 
		{
			if (path.endsWith(File.separator)) 
			{
				temp = new File(path + tempList[i]);
			}
			else 
			{
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) 
			{
				temp.delete();
			}
			if (temp.isDirectory())
			{
				delAllFile(path+"/"+ tempList[i]);//先删除文件夹里面的文件
				delFolder(path+"/"+ tempList[i]);//再删除空文件夹
			}
		}
	}

	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	/*
	public static boolean copyFile(String oldPath, String newPath) 
	{
		boolean bRet=false;
		try 
		{
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) 
			{ 
				//文件存在时
				InputStream inStream = new FileInputStream(oldPath); //读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[4096];
				//int length;
				while ( (byteread = inStream.read(buffer)) != -1) 
				{
					bytesum += byteread; //字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
				bRet = true;
			}
		}
		catch (Exception e) 
		{
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
		return bRet;
	}
	*/
	public static boolean copyFile(String oldPath, String newPath) 
	{
		boolean bRet=false;
		try 
		{
			File source = new File(oldPath);
			File dest = new File(newPath);
			FileUtils.copyFile(source, dest);
			bRet = true;
		}
		catch (Exception e) 
		{
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
		return bRet;
	}
	
	/**
	 * 复制整个文件夹内容
	 * @param oldPath String 原文件路径 如：c:/fqf
	 * @param newPath String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) 
	{
		try 
		{
			(new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
			File a=new File(oldPath);
			String[] file=a.list();
			File temp=null;
			for (int i = 0; i < file.length; i++) 
			{
				if(oldPath.endsWith(File.separator))
				{
					temp=new File(oldPath+file[i]);
				}
				else
				{
					temp=new File(oldPath+File.separator+file[i]);
				}

				if(temp.isFile())
				{
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ( (len = input.read(b)) != -1) 
					{
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if(temp.isDirectory())
				{//如果是子文件夹
					copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 移动文件到指定目录
	 * @param oldPath String 如：c:/fqf.txt
	 * @param newPath String 如：d:/fqf.txt
	 */
	public static void moveFile(String oldPath, String newPath)
	{
		copyFile(oldPath, newPath);
		delFile(oldPath);
	}

	/**
	 * 移动文件到指定目录
	 * @param oldPath String 如：c:/fqf.txt
	 * @param newPath String 如：d:/fqf.txt
	 */
	public static void moveFolder(String oldPath, String newPath)
	{
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}




	/*java中删除目录事先要删除目录下的文件或子目录。用递归就可以实现。这是我第一个用到算法作的程序，哎看来没白学。*/
	public static void del(String filepath) throws IOException
	{
		File f = new File(filepath);//定义文件路径        
		if(f.exists() && f.isDirectory())
		{	//判断是文件还是目录
			if(f.listFiles().length==0)
			{//若目录下没有文件则直接删除
				f.delete();
			}else
			{//若有则把文件放进数组，并判断是否有下级目录
				File delFile[]=f.listFiles();
				int i =f.listFiles().length;
				for(int j=0;j<i;j++)
				{
					if (delFile[j].isDirectory())
					{                                                
						del (delFile[j].getAbsolutePath());//递归调用del方法并取得子目录路径
					}
					delFile[j].delete();//删除文件
				}
			}
			del(filepath);//递归调用
		}
	}     

	/*删除一个非空目录并不是简单地创建一个文件对象，然后再调用delete()就可以完成的。要在平台无关的方式下安全地删除一个非空目录，你还需要一个算法。该算法首先删除文件，然后再从目录树的底部由下至上地删除其中所有的目录。只要简单地在目录中循环查找文件，再调用delete就可以清除目录中的所有文件：*/
	public static void emptyDirectory(File directory) 
	{
		File[ ] entries = directory.listFiles( );
		for(int i=0; i<entries.length; i++) 
		{
			entries[i].delete( );
		}
	}
	public static void emptyDirectory(String path) 
    {
        File directory = new File(path);
        if(directory.exists() == true && directory.isDirectory() == true)
        {
            File[ ] entries = directory.listFiles( );
            for(int i=0; i<entries.length; i++) 
            {
                entries[i].delete( );
            }
        }
         
    } 
	/*这个简单的方法也可以用来删除整个目录结构。当在循环中遇到一个目录时它就递归调用deleteDirectory，而且它也会检查传入的参数是否是一个真正的目录。最后，它将删除作为参数传入的整个目录。*/
	public static void deleteDirectory(File dir) throws IOException 
	{
		if( (dir == null) || !dir.isDirectory()) 
		{
			throw new IllegalArgumentException("Argument "+dir+" is not a directory. " );
		}

		File[ ] entries = dir.listFiles( );
		int sz = entries.length;

		for(int i=0; i<sz; i++) 
		{
			if(entries[i].isDirectory( )) 
			{
				deleteDirectory(entries[i]);
			} else 
			{
				entries[i].delete( );
			}
		}
		dir.delete();
	}
	
	public static List<String> getFilenameList(String path) 
    {
        List<String> list = new ArrayList<String>();
         
        File dir = new File(path);
        if( (dir == null) || !dir.isDirectory()) 
        {
            return list;
        }
 
        File[ ] entries = dir.listFiles( );
        int sz = entries.length;
 
        for(int i=0; i<sz; i++) 
        {
            if(entries[i].isDirectory() == false) 
            {
                list.add(entries[i].getName());
            }
        }
        return list;
    }
	
	public static List<String> getFileList(String path, String suffix, int maxCnt)
	{
		List<String> filelist =new ArrayList<String>();
		File dir = new File(path);
		if(dir.exists() == false)
		{
			System.out.println("--------->getFileList path is not exist, path="+path);
			return filelist;
		}
		String filepath;
		File temp = null;
		if(dir.isDirectory() == true)
		{
			String[] templist = dir.list();
			for (int i = 0; i < templist.length; i++) 
			{
				if (path.endsWith(File.separator)) 
				{
					filepath = path + templist[i];
					temp = new File(filepath);
				}
				else 
				{
					filepath = path + File.separator + templist[i];
					temp = new File(filepath);
				}
				
				if (temp.isFile())
				{//判断是否为文件
					String filename = templist[i];
					if(filename.toLowerCase().endsWith(suffix))
					{
						filelist.add(path+File.separator+templist[i]);
						if(filelist.size() >= maxCnt)
						{
							System.out.println("--------->getFileList file count="+filelist.size()+", and break");
							break;
						}
					}					
				}
			}
		}		
		
		return filelist;
	}

	public static String getBase64String(File file) throws FileNotFoundException
	{
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        byte[] buffer = new byte[(int) file.length()];
        FileInputStream inputFile = null;
        String encodeString ="";
        try {
            inputFile = new FileInputStream(file);
            inputFile.read(buffer);
            inputFile.close();
            encodeString = Base64Util.encode(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
	}

}
