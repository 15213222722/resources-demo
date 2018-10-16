package com.resourses.tool.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	
	
	public static FTPClient getConnection(FtpConfig ftpConfig){ 
		return getConnection(ftpConfig.getHost(),ftpConfig.getPort(),ftpConfig.getUserName(),ftpConfig.getPassword(),ftpConfig.getTimes());
	}
	
	/**
	 *带重试次数进行ftp连接的建立
	 * @param host 连接主机域名或IP
	 * @param port 连接端口
	 * @param userName ftp用户名
	 * @param password ftp密码
	 * @param times 重试次数
	 * @return
	 */
	public static FTPClient getConnection(String host, int port, String userName, String password, int times){ 
		FTPClient ftp = new FTPClient();
		if (times <= 0){
			times = 1;
		}
		while((times--) > 0){
			boolean isConnected = true;
			try {
				ftp.connect(host, port);
				ftp.login(userName, password);// 登录
				int reply = ftp.getReplyCode();
				//如果登录不成功(连接不上, 刚继续发起连接)
				if (!FTPReply.isPositiveCompletion(reply)) {
					isConnected = false;
					ftp.disconnect();
					Thread.sleep(2000);
					continue;
				}
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				break;
			} 
			catch (InterruptedException e) {
				isConnected = false;
			}
			catch (Exception e) {
				isConnected = false;
			}
			//连接不成功先释放连接后再重新连接
			if (!isConnected){
				try {
					ftp.disconnect();
					Thread.sleep(2000); //等待2秒再进行重连
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ftp;
	}
	
	
	/**
	 * 将inputStream 上传到服务器
	 * @param ftp
	 * @param path
	 * @param fileName
	 * @param file
	 * @return  success 成功 fail失败 
	 */
	public static String upload2FtpServer(FTPClient ftp, String path, String fileName, File file) {
		String uploadResult = "success";
		try {
			InputStream is = new FileInputStream(file);
			uploadResult = upload2FtpServer(ftp, path, fileName, is);
		}
		catch(Exception e){
			uploadResult = e.getMessage();
		}
		return uploadResult;
	}
	
	/**
	 * 将inputStream 上传到服务器
	 * @param ftpClient
	 * @param ftpPath
	 * @param fileName
	 * @param is
	 * @return  1 表示成功 0表示失败 2表示ftp断开连接
	 */
	public static String upload2FtpServer(FTPClient ftpClient, String ftpPath, String fileName, InputStream is) {
		if (null == is || StringUtils.isEmpty(ftpPath) || StringUtils.isEmpty(fileName)){
			return "fail";
		}
		
		String uploadResult = "success";
		try {
			if (!ftpClient.isConnected()){ //ftp 连接已断开
				return "fail";
			}
			
			if(!ftpPath.contains("/")){
				if(!ftpClient.changeWorkingDirectory(ftpPath)){
					ftpClient.makeDirectory(ftpPath);
					ftpClient.changeWorkingDirectory(ftpPath);
				}
			}else {
				for (String path : ftpPath.split("/")) {
					if(!ftpClient.changeWorkingDirectory(path)){
						ftpClient.makeDirectory(path);
						ftpClient.changeWorkingDirectory(path);
					}
				};
			}
			//设置成被动模式
			ftpClient.enterLocalPassiveMode();
			if (ftpClient.storeFile(fileName, is)){
				 uploadResult = "success";
			}
			else{
				 uploadResult = "fail";
			}
			is.close();
		} 
		catch (IOException e) {
			uploadResult = e.getMessage();
		}
		return uploadResult;
	}
	
	public static void deleteFile(FTPClient ftpClient, String ftpPath, String fileName)  throws Exception{
		if (StringUtils.isEmpty(ftpPath) || StringUtils.isEmpty(fileName)){
			return ;
		}
		if (!ftpClient.isConnected()){ //ftp 连接已断开
			return ;
		}
		
		if(!ftpPath.contains("/")){
			if(ftpClient.changeWorkingDirectory(ftpPath)){
					ftpClient.deleteFile(fileName);
			}
		}else {
			for (String path : ftpPath.split("/")) {
				if(!ftpClient.changeWorkingDirectory(path)){
					return ;
				}
			};
			ftpClient.deleteFile(fileName);
		}
	}
	
	public static void moveFile(FTPClient ftpClient,String from,String to){
		if(StringUtils.isEmpty(from) || StringUtils.isEmpty(to) || null == ftpClient){
			return;
		}
		if (!ftpClient.isConnected()){ //ftp 连接已断开
			return ;
		}
		
		try {
			
			if(to.lastIndexOf("/")!=-1&&StringUtils.isNotBlank(to.substring(0,to.lastIndexOf("/")))){
				String ftpPath =to.substring(0,to.lastIndexOf("/"));
				int count = 0;
				for (String path : ftpPath.split("/")) {
					count++;
					if(!ftpClient.changeWorkingDirectory(path)){
						ftpClient.makeDirectory(path);
						ftpClient.changeWorkingDirectory(path);
					}
				}
				for (int i = 0; i < count; i++) {
					ftpClient.changeToParentDirectory();
				}
				ftpClient.rename(from, to.trim());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 释放ftp连接
	 * @param ftpClient
	 */
	public static void releaseConnect(FTPClient ftpClient){
		if (null != ftpClient){
			try {
				ftpClient.quit();
				ftpClient.disconnect();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
