package com.flowable.core.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import com.flowable.common.utils.DateUtils;
import com.flowable.core.bean.BizFile;

/**
 * 文件上传工具
 * @author 26223
 *
 */
public class UploadFileUtil {
//	private static String rootPath = "E:\\temp\\workfile\\";
	private static String rootPath = "/home/ipnet/esflowFilePath/";
	/**
	 * 将上传的文件保存到磁盘，文件目录为yyyyMM/dd/yyyyMMddHHmmssSSS
	 * 
	 * @param file
	 * @return
	 */
	public static BizFile saveFile(MultipartFile file) {
		if (file == null || file.getSize() == 0) {
			return null;
		}
		Date date = new Date();
		String filePath2 = DateUtils.formatDate(date, "yyyyMM") + File.separator + DateUtils.formatDate(date, "dd") + File.separator;
//		filePath2 = filePath2 + DateUtils.formatDate(date, "yyyyMMddHHmmssSSS");
		filePath2 = filePath2 + UUID.randomUUID().toString().replaceAll("-","");
		String dp = file.getOriginalFilename();
		dp = dp.substring(dp.lastIndexOf(".") + 1);
		filePath2 = filePath2 + "." + dp;
		String filePath = rootPath + filePath2;
		File pFile = new File(filePath);
		pFile.getParentFile().mkdirs();
		BizFile bean = new BizFile();
		try { 
			file.transferTo(pFile);
			bean.setCreateDate(date);
			bean.setPath(filePath2);
			bean.setName(file.getOriginalFilename());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(bean!=null){
			//检测是否为图片
			try {  
			    Image image=ImageIO.read(pFile);  
			    if (image == null) {  
			    	bean.setFileType("FILE");
			    }  
			    else{
			    	bean.setFileType("IMAGE");
			    }
			} catch(IOException ex) {    
			}
		}
		return bean;
	}
	/**
	 * 获取到上传的文件
	 * @param bean
	 * @return
	 */
	public static File getUploadFile(BizFile bean){
		String fielPath = rootPath+bean.getPath();
		File file = new File(fielPath);
		return file;
	}
}
