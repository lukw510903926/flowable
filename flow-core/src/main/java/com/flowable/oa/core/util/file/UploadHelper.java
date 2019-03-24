package com.flowable.oa.core.util.file;

import com.flowable.oa.core.util.DateUtils;
import com.flowable.oa.core.util.IdUtil;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/24 10:57
 **/
public interface UploadHelper {

    /**
     * @param inputStream
     * @param filePath
     * @param fileName
     * @return 文件保存的路径
     */
    String upload(InputStream inputStream, String filePath, String fileName);

    /**
     * @param filePath
     * @return 文件流
     */
    InputStream download(String filePath);

    default String getFileName(String fileName) {

        String suffix = "";
        if (fileName.lastIndexOf(".") != -1) {
            suffix = fileName.substring(fileName.lastIndexOf("."));
        }
        return IdUtil.uuid() + suffix;
    }

    default String getFilePath() {

        Date date = new Date();
        return DateUtils.formatDate(date, "yyyyMM") + File.separator + DateUtils.formatDate(date, "dd") + File.separator;
    }
}
