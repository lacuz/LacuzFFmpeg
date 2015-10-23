package com.zhou.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.os.Environment;

public class FileUtil {
	private static final String appPath = "/zhou/";
	private static final String localPath = Environment
			.getExternalStorageDirectory().toString() + "/zhou/";// 保存位置
	private static final String fileName = "trace.txt";

	/**
	 * 将字符串写入文件 content 内容 file 文件 type 写入类型 1：写入开头 2：写入结尾
	 * 
	 * @throws IOException
	 **/

	public static void writeToFile(String content, int type) throws IOException {
		if(type==1)
			writeStringToFile(android.os.Build.MODEL+"\t\n"+content+"\t\n"+readFromFile());
		else
			writeStringToFile(android.os.Build.MODEL+"\t\n"+readFromFile()+content);
		
	}

	/**
	 * 一行一行读取文件，解决读取中文字符时出现乱码
	 * 
	 * 流的关闭顺序：先打开的后关，后打开的先关， 否则有可能出现java.io.IOException: Stream closed异常
	 * 
	 * @throws IOException
	 */
	public static String readFromFile() throws IOException {
		File fileParent = new File(localPath);
		if (!fileParent.exists()) {
			fileParent.mkdir();// 文件夹的创建
		}

		File file = new File(fileParent, fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileInputStream inStream = new FileInputStream(file);
		InputStreamReader streamReader = new InputStreamReader(inStream, "UTF-8");
		BufferedReader bufferReader = new BufferedReader(streamReader);
		String sumStr="";
		String line="";
		bufferReader.readLine();
		while((line= bufferReader.readLine()) !=null){
			sumStr += line+"\t\n";
		}
		bufferReader.close();
		streamReader.close();
		inStream.close();
		
		return sumStr;
	}
	
	
	
	/**
     * 一行一行写入文件，解决写入中文字符时出现乱码
     * 
     * 流的关闭顺序：先打开的后关，后打开的先关，
     *       否则有可能出现java.io.IOException: Stream closed异常
     * 
     * @throws IOException 
     */
    public static void writeStringToFile(String content) throws IOException {
        //写入中文字符时解决中文乱码问题
        FileOutputStream fos=new FileOutputStream(new File(localPath+fileName));
        OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter  bw=new BufferedWriter(osw);
        //简写如下：
        //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
        //        new FileOutputStream(new File("E:/phsftp/evdokey/evdokey_201103221556.txt")), "UTF-8"));
         bw.write(content+"\t\n");
        
        //注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
        bw.close();
        osw.close();
        fos.close();
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file要删除的根目录
	 */
	public static void deleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				deleteFile(f);
			}
			file.delete();
		}
	}

	public void copyFileToFolderAndRename(String filePath, String folderPath,
			String fileName) {
		File file = new File(filePath);
		try {
			FileInputStream input = new FileInputStream(file);
			FileOutputStream output = new FileOutputStream(folderPath + "\\"
					+ fileName);
			byte[] b = new byte[5120];
			int len;
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {

		}
	}

	public static String getAppPathStr() {
		String path = "";
		try {
			path = getStorageDirectory() + "/" + appPath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder.getAbsolutePath();
	}

	public static File getCoverPath(String imgPath) throws Exception {
		String path = getStorageDirectory() + appPath;
		String[] array = imgPath.split("\\/");
		String imgName = array[array.length - 1];
		imgPath = imgPath.replace(imgName, "");
		File folder = new File(path + imgPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}

	public static void writeInputStreamToFile(InputStream is, String path,
			String fileName) {
		File file = null;
		OutputStream output = null;
		try {
			File f = createAppPath(path);
			file = creatSDFile(f.getAbsolutePath() + fileName);

			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((is.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {

		} finally {
			try {
				output.close();
			} catch (Exception e) {

			}
		}
	}

	/*
	 * // 在SD卡指定目录上创建文件 夹 public static String creatFilePath(String filePath)
	 * throws Exception { File file = new File(getAppPath().getAbsolutePath() +
	 * filePath); if(!file.exists()){ file.mkdirs(); } return
	 * file.getAbsolutePath(); }
	 */

	// 在SD卡指定目录上创建文件
	public static File creatSDFile(String fileName) throws Exception {
		File file = new File(fileName);
		file.createNewFile();
		return file;
	}

	/*
	 * // 获取应用sd卡路径 public static String getApplicationPath() throws Exception{
	 * return getStorageDirectory() +"/"+ appPath; }
	 */

	public static File createAppPath(String filePath) throws Exception {
		String path = getStorageDirectory() + appPath + filePath;
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}

	public static File getAppPath() throws Exception {
		String path = getStorageDirectory() + "/" + appPath;
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}

	public static File getSDCardPath() throws Exception {
		String path = getStorageDirectory();
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}

	private static String getStorageDirectory() throws Exception {
		if (isSDMounted()) {
			return android.os.Environment.getExternalStorageDirectory()
					.getPath();
		} else {
			throw new Exception("SDCard not mounted!");
		}
	}

	public static boolean isSDMounted() {
		String storage_state = Environment.getExternalStorageState();
		return storage_state.contains("mounted");
	}

}
