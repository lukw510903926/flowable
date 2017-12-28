package com.flowable.web.filter;

import com.alibaba.fastjson.JSONObject;
import com.flowable.common.utils.Json;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * 校验附件大小
 * @author lukw
 * @email 13507615840@163.com
 * @create 2017-12-28 13:11
 **/
public class ValidateFileInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Long maxFileSize;

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (request instanceof MultipartHttpServletRequest) {
            long fileSize = 0L;
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            MultiValueMap<String, MultipartFile> multiValueMap = multipartHttpServletRequest.getMultiFileMap();
            if (MapUtils.isNotEmpty(multiValueMap)) {
                for (List<MultipartFile> list : multiValueMap.values()) {
                    for (MultipartFile multipartFile : list) {
                        fileSize += multipartFile.getSize();
                    }
                }
            }
            String msg = maxFileSize / 1024 / 1024 + "M";
            logger.info("附件大小 : {},可上传附件最大为 : {}", fileSize/1024/1024+"M",msg);
            if (fileSize > maxFileSize) {
                PrintWriter writer = response.getWriter();
                response.setContentType("application/json;charset=UTF-8");
                Json json = new Json("附件大小不可超过" + msg, false);
                writer.write(JSONObject.toJSONString(json));
                writer.flush();
                writer.close();
                return false;
            }
        }
        return true;
    }
}