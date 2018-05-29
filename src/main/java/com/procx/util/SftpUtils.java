package com.procx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class SftpUtils {

	private static final Logger logger = Logger.getLogger(SftpUtils.class);

	private String ip = null;
	private int port;
	private String userName = null;
	private String password = null;
	ChannelSftp sftp = null;

	public SftpUtils() {

	}

	/**
	 * 构造函数
	 * 
	 * @param ip
	 *            SFTP地址
	 * @param port
	 *            SFTP端口
	 * @param userName
	 *            登录帐号
	 * @param password
	 *            登录密码
	 */
	public SftpUtils(String ip, int port, String userName, String password) {
		this.ip = ip;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}

	public void upLoadFile(String ftpPath, String localPath, String fileName)
			throws Exception {
		try {
			sftp = this.connect(this.ip, this.port, this.userName,
					this.password);

			sftp.cd(ftpPath);
			File file = new File(localPath + fileName);
			FileInputStream fis = new FileInputStream(file);
			sftp.put(new FileInputStream(file), file.getName());
			fis.close();
			logger.info("释放连接开始");
			if (sftp.isConnected()) {
				sftp.disconnect();
			}
			logger.info("释放连接结束");
		} catch (FileNotFoundException e) {
			if (sftp != null && sftp.isConnected()) {
				sftp.disconnect();
			}
			throw new RuntimeException("FTP上传FileNotFound异常,异常原因:" + fileName
					+ e.getMessage());
		} catch (IOException e) {
			if (sftp != null && sftp.isConnected()) {
				sftp.disconnect();
			}
			throw new RuntimeException("FTP上传IO异常,异常原因:" + fileName
					+ e.getMessage());
		} catch (SftpException e) {
			if (sftp != null && sftp.isConnected()) {
				sftp.disconnect();
			}
			throw new RuntimeException("FTP上传IO异常,异常原因:" + fileName
					+ e.getMessage());
		}
	}


	public void upLoadFile(String ftpPath, String localPath, String fileName,String newFileName)throws RuntimeException {
		try {
			sftp = this.connect(this.ip, this.port, this.userName,
					this.password);

			sftp.cd(ftpPath);
			File file = new File(localPath + fileName);
			FileInputStream fis = new FileInputStream(file);
			sftp.put(new FileInputStream(file), newFileName);
			fis.close();
			logger.info("释放连接开始");
			if (sftp.isConnected()) {
				sftp.disconnect();
			}
			logger.info("释放连接结束");
		} catch (FileNotFoundException e) {
			if (sftp != null && sftp.isConnected()) {
				sftp.disconnect();
			}
			throw new RuntimeException("FTP上传FileNotFound异常,异常原因:" + fileName
					+ e.getMessage());
		} catch (IOException e) {
			if (sftp != null && sftp.isConnected()) {
				sftp.disconnect();
			}
			throw new RuntimeException("FTP上传IO异常,异常原因:" + fileName
					+ e.getMessage());
		} catch (SftpException e) {
			if (sftp != null && sftp.isConnected()) {
				sftp.disconnect();
			}
			throw new RuntimeException("FTP上传IO异常,异常原因:" + fileName
					+ e.getMessage());
		}
	}

	public void upLoadFile(InputStream is,String ftpPath, String fileName)
			throws RuntimeException {
		try {
			sftp = this.connect(this.ip, this.port, this.userName,
					this.password);
			try {
				sftp.cd(ftpPath);
			} catch (SftpException e) {
				createDir(ftpPath, sftp);
				sftp.cd(ftpPath);
			}

			sftp.put(is, fileName);

			logger.info("释放连接开始");
			if (sftp.isConnected()) {
				sftp.disconnect();
			}
			logger.info("释放连接结束");
		} catch (SftpException e) {
			if (sftp != null && sftp.isConnected()) {
				sftp.disconnect();
			}
			e.printStackTrace();
			throw new RuntimeException("FTP上传IO异常,异常原因:" + fileName
					+ e.getMessage());
		}
	}

	/**
	 * 创建一个文件目录
	 */
	private void createDir(String createpath, ChannelSftp sftp) throws RuntimeException {
		try {
			if (isDirExist(createpath)) {
				this.sftp.cd(createpath);
			}
			String pathArry[] = createpath.split("/");
			StringBuffer filePath = new StringBuffer("/");
			for (String path : pathArry) {
				if (path.equals("")) {
					continue;
				}
				filePath.append(path + "/");
				if (isDirExist(filePath.toString())) {
					sftp.cd(filePath.toString());
				} else {
					// 建立目录
					sftp.mkdir(filePath.toString());
					// 进入并设置为当前目录
					sftp.cd(filePath.toString());
				}
			}
			this.sftp.cd(createpath);
		} catch (SftpException e) {
			throw new RuntimeException("创建路径错误：" + createpath);
		}
	}

	/**
	 * 判断目录是否存在
	 */
	private boolean isDirExist(String directory) {
		boolean isDirExistFlag = false;
		try {
			SftpATTRS sftpATTRS = sftp.lstat(directory);
			isDirExistFlag = true;
			return sftpATTRS.isDir();
		} catch (Exception e) {
			if (e.getMessage().toLowerCase().equals("no such file")) {
				isDirExistFlag = false;
			}
		}
		return isDirExistFlag;
	}

	private ChannelSftp connect(String ip, int port, String username,
			String password) {
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			Session sshSession = jsch.getSession(username, ip, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			System.out.println("链接成功");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			System.out.println("Connected to " + ip + ".");
		} catch (Exception e) {
			logger.error(e);
		}
		return sftp;
	}

	/**
	 * 下载文件
	 * 
	 * @param directory
	 *            下载目录
	 * @param downloadFile
	 *            下载的文件
	 * @param saveFile
	 *            存在本地的路径
	 * @param sftp
	 * @throws FtpException
	 */
	public List<String> downloadFiles(String ftpPath, String localPath,
			String fileName) throws RuntimeException {
		List<String> downloadSuccessFileNames = new ArrayList<String>();
		try {
			sftp = this.connect(this.ip, this.port, this.userName,
					this.password);

			sftp.cd(ftpPath);
			File file = new File(localPath + fileName);
			FileOutputStream fosTream = new FileOutputStream(file);
			InputStream inStream = sftp.get(fileName);

			sftp.get(fileName, fosTream);
			fosTream.close();
			downloadSuccessFileNames.add(fileName); // 下载成功的文件名
			logger.info("释放连接开始");
			if (sftp.isConnected()) {
				sftp.disconnect();
			}
			logger.info("释放连接结束");
		} catch (FileNotFoundException e) {
			if (sftp != null && sftp.isConnected()) {
				sftp.disconnect();
			}
			throw new RuntimeException("FTP上传FileNotFound异常,异常原因:" + fileName
					+ e.getMessage());
		} catch (IOException e) {
			if (sftp != null && sftp.isConnected()) {
				sftp.disconnect();
			}
			throw new RuntimeException("FTP上传IO异常,异常原因:" + fileName
					+ e.getMessage());
		} catch (SftpException e) {
			if (sftp != null && sftp.isConnected()) {
				sftp.disconnect();
			}
			throw new RuntimeException("FTP上传IO异常,异常原因:" + fileName
					+ e.getMessage());
		}
		return downloadSuccessFileNames;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
