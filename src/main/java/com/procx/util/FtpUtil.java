package com.procx.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * Ftp操作工具类
 * 
 * @author easonwu 
 *
 */
public class FtpUtil {

	private  String userName = "bi";
	private  String passWord = "bi";
	private  String hostName = "10.40.197.41" ;
	private  String port = "" ;
	private  String workDir = "test/download" ;
	private  FTPClient ftpClient = new FTPClient();
	

	public FtpUtil() throws IOException {
		initailCheck() ;
	}
	
	public FtpUtil(String hostName , String port , String userName 
			, String passWord , String workDir) throws IOException {
		this.hostName = hostName ;
		this.port = port ;
		this.userName = userName ;
		this.passWord = passWord ;
		this.workDir = workDir ;
		initailCheck() ;
	}
	
	private void initailCheck() throws IOException {
		connectToServer();
		if( this.workDir != null && !"".endsWith(this.workDir )){
			checkPathExist(this.workDir);
		}
		closeConnect();
	}

	/** 
	 * 查找指定目录是否存在 
	 * @param String filePath 要查找的目录 
	 * @return boolean:存在:true，不存在:false 
	 * @throws IOException 
	 */
	private  boolean checkPathExist(String filePath) throws IOException {
		boolean existFlag = false;
		try {
			if (!ftpClient.changeWorkingDirectory(filePath)) {
				ftpClient.makeDirectory(filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return existFlag;
	}

	/** 
	 * 连接到ftp服务器 
	 */
	private void connectToServer() throws IOException{
		if(!ftpClient.isConnected()){
			int reply;
			try{
				ftpClient = new FTPClient();
				ftpClient.enterLocalPassiveMode();
				
				if(this.port == null || "".equals(this.port)){
					ftpClient.connect(this.hostName);
				}
				else{
					ftpClient.connect(this.hostName, Integer.parseInt(this.port));
				}
				
				ftpClient.login(userName, passWord);
				reply = ftpClient.getReplyCode();
				
				if(!FTPReply.isPositiveCompletion(reply)){
					ftpClient.disconnect();
					System.err.println("FTP server refused connection.");
				}
			}
			catch(IOException e){
//				System.err.println("登录ftp服务器【" + this.hostName + "】失败");
				e.printStackTrace();
				throw new IOException("登录ftp服务器【" + this.hostName + "】失败");
			}
		}
	}

	/** 
	 * 关闭连接 
	 */
	private  void closeConnect() {
		try {
			if (ftpClient != null) {
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	 * 转码[GBK ->  ISO-8859-1] 
	 * 不同的平台需要不同的转码 
	 * @param obj 
	 * @return 
	 */
	private static String gbkToIso8859(Object obj) {
		try {
			if (obj == null)
				return "";
			else
				return new String(obj.toString().getBytes("GBK"), "iso-8859-1");
		} catch (Exception e) {
			return "";
		}
	}

	/** 
	 * 转码[ISO-8859-1 ->  GBK] 
	 * 不同的平台需要不同的转码 
	 * @param obj 
	 * @return 
	 */
	private static String iso8859ToGbk(Object obj) {
		try {
			if (obj == null)
				return "";
			else
				return new String(obj.toString().getBytes("iso-8859-1"), "GBK");
		} catch (Exception e) {
			return "";
		}
	}

	/** 
	 * 设置传输文件的类型[文本文件或者二进制文件] 
	 * @param fileType--BINARY_FILE_TYPE、ASCII_FILE_TYPE 
	 */
	private  void setFileType(int fileType) {
		try {
			ftpClient.setFileType(fileType);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void changeDir(String filePath) throws IOException {
//		跳转到指定的文件目录 
		if (filePath != null && !filePath.equals("")) {
			if (filePath.indexOf("/") != -1) {
				int index = 0;
				while ((index = filePath.indexOf("/")) != -1) {
					System.out.println("P:"+ filePath.substring(0,
							index)) ;
					ftpClient.changeWorkingDirectory(filePath.substring(0,
							index));

					filePath = filePath.substring(index + 1, filePath.length());
				}
				if (!filePath.equals("") && !"/".equals(filePath)) {
					ftpClient.changeWorkingDirectory(filePath);
				}
			} else {
				ftpClient.changeWorkingDirectory(filePath);
			}
		}
	}
	/**
	 * check file 
	 * @param filePath
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private  boolean checkFileExist(String filePath, String fileName)
			throws IOException {
		boolean existFlag = false;
		changeDir( filePath )  ;
		String[] fileNames = ftpClient.listNames();
		if (fileNames != null && fileNames.length > 0) {
			for (int i = 0; i < fileNames.length; i++) {
				System.out.println("File:" + iso8859ToGbk(fileNames[i])) ;
				if (fileNames[i] != null
						&& iso8859ToGbk(fileNames[i]).equals(fileName)) {
					existFlag = true;
					break;
				}
			}
		}
		ftpClient.changeToParentDirectory();
		return existFlag;
	}

	/**
	 * download ftp file as inputstream 
	 * @param remoteFilePath 
	 * @param remoteFileName
	 * @return
	 * @throws IOException
	 */
	public  InputStream downloadFile(
			String remoteFileName) throws IOException {
		InputStream returnValue = null;
		//下载文件 
		BufferedOutputStream buffOut = null;
		try {
			//连接ftp服务器 
			connectToServer();
			if (!checkFileExist(this.workDir, remoteFileName)) {
				System.out.println("<----------- ERR : file  " + this.workDir	+ "/" + remoteFileName+ " does not exist, download failed!----------->");
				return null;
			} else {
				changeDir( this.workDir )  ;
				String[] fileNames = ftpClient.listNames();
				
				//设置传输二进制文件 
				setFileType(FTP.BINARY_FILE_TYPE);
				//获得服务器文件 
				InputStream in = ftpClient.retrieveFileStream(remoteFileName);
				//输出操作结果信息 
				if (in != null) {
					returnValue = new ByteArrayInputStream(IOUtils.toByteArray(in));
					in.close();
					System.out.println("<----------- INFO: download "+ this.workDir + "/" + remoteFileName+ " from ftp ： succeed! ----------->");
				} else {
					System.out.println("<----------- ERR : download "+ this.workDir + "/" + remoteFileName+ " from ftp : failed! ----------->");
				}
			}
			//关闭连接 
			closeConnect();
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = null;
			//输出操作结果信息 
			System.out.println("<----------- ERR : download " +  this.workDir + "/" + remoteFileName+ " from ftp : failed! ----------->");
		} finally {
			try {
				if (ftpClient.isConnected()) {
					closeConnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}
	
	/**
	 * 
	 * @param remoteFilePath
	 * @param remoteFileName
	 * @param localFileName
	 * @return
	 * @throws IOException
	 */
	public  boolean downloadFile(String remoteFilePath,
			String remoteFileName, String localFileName) throws IOException {
		boolean returnValue = false;
		//下载文件 
		BufferedOutputStream buffOut = null;
		try {
			//连接ftp服务器 
			connectToServer();
			File localFile = new File(localFileName.substring(0, localFileName
					.lastIndexOf("/")));
			if (!localFile.exists()) {
				localFile.mkdirs();
			}
			if (!checkFileExist(remoteFilePath, remoteFileName)) {
				System.out.println("<----------- ERR : file  " + remoteFilePath	+ "/" + remoteFileName+ " does not exist, download failed!----------->");
				return false;
			} else {
				changeDir( remoteFilePath )  ;
				String[] fileNames = ftpClient.listNames();
				
				//设置传输二进制文件 
				setFileType(FTP.BINARY_FILE_TYPE);
				//获得服务器文件 
				buffOut = new BufferedOutputStream(new FileOutputStream(
						localFileName));
				returnValue = ftpClient.retrieveFile(
						remoteFileName, buffOut);
				//输出操作结果信息 
				if (returnValue) {
					System.out.println("<----------- INFO: download "+ remoteFilePath + "/" + remoteFileName+ " from ftp ： succeed! ----------->");
				} else {
					System.out.println("<----------- ERR : download "+ remoteFilePath + "/" + remoteFileName+ " from ftp : failed! ----------->");
				}
			}
			//关闭连接 
			closeConnect();
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = false;
			//输出操作结果信息 
			System.out.println("<----------- ERR : download " + remoteFilePath+ "/" + remoteFileName+ " from ftp : failed! ----------->");
		} finally {
			try {
				if (buffOut != null) {
					buffOut.close();
				}
				if (ftpClient.isConnected()) {
					closeConnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}
	
	/**
	 * 
	 * @param remoteFileNameList
	 * @return
	 * @throws IOException
	 */
	public  List batchDownloadFile(
			List remoteFileNameList) throws IOException {
		InputStream resultIs = null ;
		List inputStreamList = null ;
		String remoteFileName = null ;
		
		if( remoteFileNameList == null ||remoteFileNameList.isEmpty() ) return null ;
			
		inputStreamList = new ArrayList() ;
		for( Iterator it = remoteFileNameList.iterator() ; it.hasNext() ; ) {
			remoteFileName = (String)it.next() ;
			resultIs = this.downloadFile(remoteFileName);
			if (resultIs != null ) {
				inputStreamList.add(resultIs) ;
			} 
		}
		return inputStreamList;
	}
	
	
	/**
	 * 删除服务器上文件
	 * 
	 * @param fileDir
	 *            文件路径
	 * @param fileName
	 *            文件名称
	 * @throws IOException
	 */
	public boolean delFile(String fileDir, String fileName) throws IOException {
		boolean returnValue = false;
		try {
			//连接ftp服务器 
			connectToServer();
			//跳转到指定的文件目录 
			if (fileDir != null) {
				if (fileDir.indexOf("/") != -1) {
					int index = 0;
					while ((index = fileDir.indexOf("/")) != -1) {
						ftpClient.changeWorkingDirectory(fileDir.substring(0,
								index));
						fileDir = fileDir
								.substring(index + 1, fileDir.length());
					}
					if (!fileDir.equals("")) {
						ftpClient.changeWorkingDirectory(fileDir);
					}
				} else {
					ftpClient.changeWorkingDirectory(fileDir);
				}
			}
			//设置传输二进制文件 
			setFileType(FTP.BINARY_FILE_TYPE);
			//获得服务器文件 
			returnValue = ftpClient.deleteFile(fileName);
			//关闭连接 
			closeConnect();
			//输出操作结果信息 
			if (returnValue) {
				System.out.println("<----------- INFO: delete " + fileDir + "/"
						+ fileName + " at ftp:succeed! ----------->");
			} else {
				System.out.println("<----------- ERR : delete " + fileDir + "/"
						+ fileName + " at ftp:failed! ----------->");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = false;
			//输出操作结果信息 
			System.out.println("<----------- ERR : delete " + fileDir + "/"
					+ fileName + " at ftp:failed! ----------->");
		} finally {
			try {
				if (ftpClient.isConnected()) {
					closeConnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	/**
	 * upload file to ftp 
	 * @param uploadFile
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public boolean uploadFile(String uploadFile , String fileName ) throws IOException{
		if(uploadFile == null || "".equals(uploadFile.trim())){
			System.out.println("<----------- ERR :  uploadFile:" + uploadFile
					+ " is null , upload failed! ----------->");
			return false ;
		}
		return this.uploadFile(new File(uploadFile) , fileName ) ;
	}
	
	
	/**
	 * batch upload files 
	 * @param uploadFiles
	 * @return
	 * @throws IOException
	 */
	public boolean batchUploadFile(List uploadFileList ) throws IOException{
		if(uploadFileList == null || uploadFileList.isEmpty()){
			System.out.println("<----------- ERR :  batchUploadFile failed! because the file list is empty ! ----------->");
			return false ;
		}
		
		for( Iterator it = uploadFileList.iterator() ; it.hasNext() ;){
			File uploadFile = (File)it.next() ;
			if( !uploadFile(uploadFile , uploadFile.getName() ) ){
				System.out.println("<----------- ERR :  upload file【"+uploadFile.getName()+"】  failed! ----------->") ;
				return false ;
			}
		}
		return true ;
	}
	
	/**
	 * upload file to ftp 
	 * @param uploadFile
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public  boolean uploadFile(File uploadFile, String fileName)
			throws IOException {
		if (!uploadFile.exists()) {
			System.out.println("<----------- ERR : an named " + fileName
					+ " not exist, upload failed! ----------->");
			return false;
		} 
		return this.uploadFile(new FileInputStream(uploadFile) , fileName ) ;
	}

	/**
	 * upload file to ftp 
	 * @param is
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public boolean uploadFile(InputStream is , String fileName ) throws IOException {
		boolean returnValue = false;
		// 上传文件
		BufferedInputStream buffIn = null;
		try {
		
				// 建立连接
				connectToServer();
				// 设置传输二进制文件
				setFileType(FTP.BINARY_FILE_TYPE);
				// 获得文件
				buffIn = new BufferedInputStream(is);
				// 上传文件到ftp
				ftpClient.enterLocalPassiveMode();
				returnValue = ftpClient.storeFile(gbkToIso8859(this.workDir + "/"
						+ fileName), buffIn);
				// 输出操作结果信息
				if (returnValue) {
					System.out.println("<----------- INFO: upload file  to ftp : succeed! ----------->");
				} else {
					System.out.println("<----------- ERR : upload file  to ftp : failed! ----------->");
				}
				// 关闭连接
				closeConnect();
			
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = false;
			System.out.println("<----------- ERR : upload file  to ftp : failed! ----------->");
		} finally {
			try {
				if (buffIn != null) {
					buffIn.close();
				}
				if (ftpClient.isConnected()) {
					closeConnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnValue ;
	}
	
	
	  public boolean changeDirectory(String path) throws IOException {   
		  return ftpClient.changeWorkingDirectory(path);   
	    }   
	    public boolean createDirectory(String pathName) throws IOException {   
	        return ftpClient.makeDirectory(pathName);   
	    }   
	    public boolean removeDirectory(String path) throws IOException {   
	        return ftpClient.removeDirectory(path);   
	    } 
	    // delete all subDirectory and files.   
	    public boolean removeDirectory(String path, boolean isAll)   
	            throws IOException {   
	           
	        if (!isAll) {   
	            return removeDirectory(path);   
	        }   
	  
	        FTPFile[] ftpFileArr = ftpClient.listFiles(path);   
	        if (ftpFileArr == null || ftpFileArr.length == 0) {   
	            return removeDirectory(path);   
	        }   
	        //    
	        for (int i=ftpFileArr.length ; i>0 ; i-- ) {   
	        	FTPFile ftpFile = ftpFileArr[i-1] ; 
	            String name = ftpFile.getName();   
	            if (ftpFile.isDirectory()) {   
	System.out.println("* [sD]Delete subPath ["+path + "/" + name+"]");                
	                removeDirectory(path + "/" + name, true);   
	            } else if (ftpFile.isFile()) {   
	System.out.println("* [sF]Delete file ["+path + "/" + name+"]");                           
	                deleteFile(path + "/" + name);   
	            }
//	            else if (ftpFile.isSymbolicLink()) {   
//	  
//	            } else if (ftpFile.isUnknown()) {   
//	  
//	            }   
	        }   
	        return ftpClient.removeDirectory(path);   
	    }   

	    public boolean deleteFile(String pathName) throws IOException {   
	        return ftpClient.deleteFile(pathName);   
	    }   
	    
	    public List getFileList(String path) throws IOException {   
	        FTPFile[] ftpFiles= ftpClient.listFiles(path);   
	           
	        List retList = new ArrayList();   
	        if (ftpFiles == null || ftpFiles.length == 0) {   
	            return retList;   
	        }   
	        for (int i=ftpFiles.length ; i>0 ; i-- ) {   
	        	FTPFile ftpFile = ftpFiles[i-1] ;
	        	  if (ftpFile.isFile()) {   
		                retList.add(ftpFile.getName());   
		            }   
	        }
	           
	        return retList;   
	    } 
	    
	    private void changeWordDir(String wd) throws IOException{
			if(wd != null && wd != ""){
				if(!ftpClient.changeWorkingDirectory(wd)){
					ftpClient.makeDirectory(wd);
					ftpClient.changeWorkingDirectory(wd);
				}
			}
		}
	    
	    /**
	     * 上传文件到服务器上
	     * @param is 文件流
	     * @param filepath FTP服务器上文件路径
	     * @param filename 文件名称
	     * @return
	     * @throws Exception
	     */
	    public boolean uploadFile(InputStream is,String filepath,String filename) throws Exception{
			if(!ftpClient.isConnected()){
				connectToServer();
			}
			changeWordDir(this.workDir);
			changeWordDir(filepath);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE); 
			ftpClient.enterLocalPassiveMode();
			boolean isSuccess = ftpClient.storeFile(filename, is);
			if(isSuccess){
    			System.out.println("<-- INFO:upload "+workDir+"/"+filepath+"/"+filename+" from ftp: succeed -->");
    		}else{
    			System.out.println("<-- INFO:upload "+workDir+"/"+filepath+"/"+filename+" from ftp: failed -->");
    		}
			is.close();
			closeConnect();
			return isSuccess;
		}
	    
	    /**
	     * 从FTP服务器上下载文件
	     * @param filepath FTP服务器文件路径
	     * @param filename 文件名称
	     * @param localPath 本机文件路径
	     * @return
	     * @throws IOException
	     */
	    public boolean downLoadFile(String filepath,String filename,String localPath) throws IOException{
			if(!ftpClient.isConnected()){
				connectToServer();
			}
			changeWordDir(this.workDir);
			changeWordDir(filepath);
			if(ftpClient.listNames(filename).length > 0){
				setFileType(FTP.BINARY_FILE_TYPE);
				File localFile = new File(localPath+"/"+filename);
				OutputStream os = new FileOutputStream(localFile);   
	        	boolean isSuccess = ftpClient.retrieveFile(filename, os);  
	        	if(isSuccess){
	    			System.out.println("<-- INFO:download "+workDir+"/"+filepath+"/"+filename+" from ftp: succeed -->");
	    		}else{
	    			System.out.println("<-- INFO:download "+workDir+"/"+filepath+"/"+filename+" from ftp: failed -->");
	    		}
	        	os.close();
	        	closeConnect();
	        	return isSuccess;
			}else{
				System.out.println("<-- INFO:download "+workDir+"/"+filepath+"/"+filename+" from ftp: failed, file is not exist -->");
			}
			closeConnect();
			return false;
		}
		
	    /**
	     * 删除FTP服务器上的文件
	     * @param filepath 文件路径
	     * @param filename 文件名称
	     * @throws IOException
	     */
		public boolean deleteFile(String filepath,String filename) throws IOException{
			if(!ftpClient.isConnected()){
				connectToServer();
			}
			changeWordDir(this.workDir);
			changeWordDir(filepath);
			boolean isSuccess = false;
			if(ftpClient.listNames(filename).length > 0){
				isSuccess = ftpClient.deleteFile(filename);
				if(isSuccess){
					System.out.println("<-- INFO:delete "+workDir+"/"+filepath+"/"+filename+" from ftp: succeed -->");
				}else{
					System.out.println("<-- INFO:delete "+workDir+"/"+filepath+"/"+filename+" from ftp: failed -->");
				}
			}else{
				System.out.println("<-- INFO:delete "+workDir+"/"+filepath+"/"+filename+" from ftp: failed, file is not exist -->");
			}
			closeConnect();
			return isSuccess;
		}
	    
}
