package com.flowable.oa.core.util.file;

import com.flowable.oa.core.util.IdUtil;
import com.flowable.oa.core.util.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/24 11:05
 **/
@Slf4j
public class DiskUploadHelper implements UploadHelper {

    @Override
    public String upload(InputStream inputStream, String filePath, String fileName) {

        try {
            String suffix = "";
            if (fileName.lastIndexOf(".") != -1) {
                suffix = fileName.substring(fileName.lastIndexOf("."));
            }
            String name = filePath + File.separator + getFilePath() + IdUtil.uuid();
            File newFile = File.createTempFile(name, suffix);
            FileUtils.copyInputStreamToFile(inputStream, newFile);
            return name + suffix;
        } catch (Exception e) {
            log.error("文件保存失败 :{}", e);
            throw new ServiceException("文件保存失败!");
        }
    }

    @Override
    public InputStream download(String filePath) {

        try {
            File file = new File(filePath);
            return FileUtils.openInputStream(file);
        } catch (IOException e) {
            log.error("文件下载失败 : {}", e);
            throw new ServiceException("文件下载失败!");
        }
    }
}
