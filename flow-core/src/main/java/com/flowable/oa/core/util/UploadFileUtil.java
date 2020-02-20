package com.flowable.oa.core.util;

import com.flowable.oa.core.entity.BizFile;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传工具
 *
 * @author 26223
 */
@Slf4j
public class UploadFileUtil {

    /**
     * 将上传的文件保存到磁盘，文件目录为yyyyMM/dd/yyyyMMddHHmmssSSS
     *
     * @param file
     * @return
     */
    public static BizFile saveFile(MultipartFile file, String bizFileRootPath) {

        if (file == null || file.getSize() == 0) {
            return null;
        }
        Date date = new Date();
        String filePath2 = DateUtils.formatDate(date, "yyyyMM") + File.separator + DateUtils.formatDate(date, "dd") + File.separator;
        filePath2 = filePath2 + UUID.randomUUID().toString().replaceAll("-", "");
        String dp = file.getOriginalFilename();
        dp = dp.substring(dp.lastIndexOf(".") + 1);
        filePath2 = filePath2 + "." + dp;
        String filePath = bizFileRootPath + filePath2;
        File pFile = new File(filePath);
        pFile.getParentFile().mkdirs();
        BizFile bean = new BizFile();
        try {
            file.transferTo(pFile);
            bean.setCreateDate(date);
            bean.setPath(filePath2);
            bean.setName(file.getOriginalFilename());
        } catch (IOException | IllegalStateException e) {
            log.error("文件上传失败 : ", e);
        }
        try {
            Image image = ImageIO.read(pFile);
            if (image == null) {
                bean.setFileType("FILE");
            } else {
                bean.setFileType("IMAGE");
            }
        } catch (IOException ex) {
            bean.setFileType("FILE");
            log.error("获取文件类型失败 : ", ex);
        }
        return bean;
    }

    /**
     * 获取到上传的文件
     *
     * @param bean
     * @return
     */
    public static File getUploadFile(BizFile bean, String bizFileRootPath) {
        return new File(bizFileRootPath + bean.getPath());
    }
}
