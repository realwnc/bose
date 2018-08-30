package com.mids.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

//import org.apache.commons.lang.StringUtils;
 
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
 
/**
 * 文件工具类.
 * 1.所有的文件路径必须以'/'开头和结尾，否则路径最后一部分会被当做是文件名
 * 2.方法出现异常的时候，会关闭sftp连接（但是不会关闭session和channel），异常会抛出
 * @author wncheng
 */
public class MySftpFileUtils {
 
    /**
     * 文件路径前缀. /work
     */
    //private static final String ROOT_PATH = "/work";
	private static final String ROOT_PATH = "";
 
    /**
     * sftp连接池. key = host + "," + port + "," + username + "," + password;
     */
    private static final Map<String, Channel> SFTP_CHANNEL_POOL = new HashMap<String, Channel>();
 
    /**
     * 获取sftp协议连接.
     * @param host 主机名
     * @param port 端口
     * @param username 用户名
     * @param password 密码
     * @return 连接对象
     * @throws JSchException 异常
     */
    public static ChannelSftp getSftpConnect(final String host, final int port, final String username, final String password) throws JSchException {
        Session sshSession = null;
        Channel channel = null;
        ChannelSftp sftp = null;
        String key = host + "," + port + "," + username + "," + password;
        if (null == SFTP_CHANNEL_POOL.get(key)) 
        {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            channel = sshSession.openChannel("sftp");
            channel.connect();
            SFTP_CHANNEL_POOL.put(key, channel);
            System.out.println("--------->getSftpConnect create new channel for host="+host);
        } else {
            channel = SFTP_CHANNEL_POOL.get(key);
            sshSession = channel.getSession();
            if (!sshSession.isConnected())
                sshSession.connect();
            if (!channel.isConnected())
                channel.connect();
            System.out.println("--------->getSftpConnect get exist channel for host="+host);
        }
        sftp = (ChannelSftp) channel;
        return sftp;
    }
 
    /**
     * 下载文件-sftp协议.
     * @param remoteFile 下载的文件
     * @param localFile 存在本地的路径 c:\\1.txt
     * @param sftp sftp连接
     * @return 文件
     * @throws Exception 异常
     * download("/work/sftp_data/1.txt", "D:\\logsecklogpath\\10.93.10.237\\2.txt", sftp);
     */
    public static File download(final String remoteFile, final String localFile, final ChannelSftp sftp) throws Exception {
        FileOutputStream os = null;
        File file = new File(localFile);
        try {
            if (file.exists() == false) 
            {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) 
                {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
            os = new FileOutputStream(file);
            List<String> list = formatPath(remoteFile);
            sftp.get(list.get(0) + list.get(1), os);
        } catch (Exception e) {
        	disconnect(sftp);
            e.getMessage();
            throw e;
        } finally {
            os.close();
        }
        return file;
    }
 
    /**
     * 下载文件-sftp协议.
     * @param downloadFile 下载的文件
     * @param saveFile 存在本地的路径
     * @param sftp sftp连接
     * @return 文件 byte[]
     * @throws Exception 异常
     */
    @Deprecated
    public static byte[] downloadAsByte(final String downloadFile, final ChannelSftp sftp) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            List<String> list = formatPath(downloadFile);
            sftp.get(list.get(0) + list.get(1), os);
        } catch (Exception e) {
        	e.printStackTrace();
        	disconnect(sftp);
            throw e;
        } finally {
            os.close();
        }
        return os.toByteArray();
    }
 
    /**
     * 删除文件-sftp协议.
     * @param remoteFile 要删除的文件 /work/sftp_data/1.txt
     * @param sftp sftp连接
     * @throws Exception 异常
     */
    public static void removeFile(final String remoteFile, final ChannelSftp sftp) throws Exception {
        try {
            List<String> list = formatPath(remoteFile);
            sftp.rm(list.get(0) + list.get(1));
        } catch (Exception e) {
        	disconnect(sftp);
        	e.printStackTrace();
            throw e;
        }
    }
 
    /**
     * 删除文件夹-sftp协议.如果文件夹有内容，则会抛出异常.
     * @param remotePath 文件夹路径 /work/sftp_data/test/
     * @param sftp sftp连接
     * @param resursion 递归删除
     * @throws Exception 异常
     */
    public static void removePath(final String remotePath, final ChannelSftp sftp, final boolean recursion) throws Exception {
        try {
            String fp = formatPath(remotePath).get(0);
            if (recursion)
            	exeRemovePathRec(fp, sftp);
            else
                sftp.rmdir(fp);
        } catch (Exception e) {
        	disconnect(sftp);
        	e.printStackTrace();
            throw e;
        }
    }
 
    /**
     * 递归删除执行.
     * @param remotePath 文件路径 /work/sftp_data/test/
     * @param sftp sftp连接
     * @throws SftpException
     */
    private static void exeRemovePathRec(final String remotePath, final ChannelSftp sftp) throws SftpException {
        @SuppressWarnings("unchecked")
        Vector<LsEntry> vector = sftp.ls(remotePath);
        if (vector.size() == 1) { // 文件，直接删除
            sftp.rm(remotePath);
        } else if (vector.size() == 2) { // 空文件夹，直接删除
            sftp.rmdir(remotePath);
        } else {
            String fileName = "";
            // 删除文件夹下所有文件
            for (LsEntry en : vector) {
                fileName = en.getFilename();
                if (".".equals(fileName) || "..".equals(fileName)) {
                    continue;
                } else {
                	exeRemovePathRec(remotePath + "/" + fileName, sftp);
                }
            }
            // 删除文件夹
            sftp.rmdir(remotePath);
        }
    }
 
    /**
     * 上传文件-sftp协议.
     * @param localFile 源文件 c:\\1.txt
     * @param removePath 保存路径
     * @param removeFilename 保存文件名
     * @param sftp sftp连接
     * @throws Exception 异常
     * uploadFile("D:\\logsecklogpath\\10.93.10.237\\2.txt", "/work/sftp_data", "1.txt", sftp);
     */
    public static void uploadFile(final String localFile, final String removePath, final String removeFilename, final ChannelSftp sftp)
            throws Exception {
        mkdir(removePath, sftp);
        sftp.cd(removePath);
        sftp.put(localFile, removeFilename);
    }
 
 
    /**
     * 根据路径创建文件夹.
     * @param dir 路径 必须是 /xxx/xxx/ 不能就单独一个/
     * @param sftp sftp连接
     * @throws Exception 异常
     */
    public static boolean mkdir(final String dir, final ChannelSftp sftp) throws Exception {
        try {
            if (StringUtils.isBlank(dir))
                return false;
            String md = dir.replaceAll("\\\\", "/");
            if (md.indexOf("/") != 0 || md.length() == 1)
                return false;
            return mkdirs(md, sftp);
        } catch (Exception e) {
        	disconnect(sftp);
        	e.printStackTrace();
            throw e;
        }
    }
 
    /**
     * 递归创建文件夹.
     * @param dir 路径
     * @param sftp sftp连接
     * @return 是否创建成功
     * @throws SftpException 异常
     */
    private static boolean mkdirs(final String dir, final ChannelSftp sftp) throws SftpException {
        String dirs = dir.substring(1, dir.length() - 1);
        String[] dirArr = dirs.split("/");
        String base = "";
        for (String d : dirArr) {
            base += "/" + d;
            if (dirExist(base + "/", sftp)) {
                continue;
            } else {
                sftp.mkdir(base + "/");
            }
        }
        return true;
    }
 
    /**
     * 判断文件夹是否存在.
     * @param dir 文件夹路径， /xxx/xxx/
     * @param sftp sftp协议
     * @return 是否存在
     */
    private static boolean dirExist(final String dir, final ChannelSftp sftp) {
        try {
        	@SuppressWarnings("unchecked")
            Vector<LsEntry> vector = sftp.ls(dir);
            if (null == vector)
                return false;
            else
                return true;
        } catch (SftpException e) {
        	e.printStackTrace();
            return false;
        }
    }
 
    /**
     * 格式化路径.
     * @param srcPath 原路径. /xxx/xxx/xxx.yyy 或 X:/xxx/xxx/xxx.yy
     * @return list, 第一个是路径（/xxx/xxx/）,第二个是文件名（xxx.yy）
     */
    public static List<String> formatPath(final String srcPath) {
        List<String> list = new ArrayList<String>(2);
        String repSrc = srcPath.replaceAll("\\\\", "/");
        int firstP = repSrc.indexOf("/");
        int lastP = repSrc.lastIndexOf("/");
        String fileName = lastP + 1 == repSrc.length() ? "" : repSrc.substring(lastP + 1);
        String dir = firstP == -1 ? "" : repSrc.substring(firstP, lastP);
        dir = ROOT_PATH + (dir.length() == 1 ? dir : (dir + "/"));
        list.add(dir);
        list.add(fileName);
        return list;
    }
 
    /**
     * 关闭协议-sftp协议.
     * @param sftp sftp连接
     */
    public static void disconnect(final ChannelSftp sftp) {
        sftp.exit();
    }
 
    public static void main(String[] args) throws Exception {
    	try
    	{
    		ChannelSftp sftp = getSftpConnect("10.93.10.237", 22, "root", "3311899");
            //download("/work/sftp_data/1.txt", "D:\\logsecklogpath\\10.93.10.237\\2.txt", sftp);
            //removeFile("/work/sftp_data/1.txt", sftp);
            //removePath("/work/sftp_data/test/", sftp, false);
    		uploadFile("D:\\logsecklogpath\\10.93.10.237\\2.txt", "/work/sftp_data", "1.txt", sftp);
            disconnect(sftp);
    	}
        catch(Exception e)
    	{
        	e.printStackTrace();
    	}
        System.exit(0);
    }
}