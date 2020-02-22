package com.flowable.oa.api.controller;

import com.flowable.oa.core.util.RestResult;
import com.flowable.oa.core.util.WebUtil;
import com.flowable.oa.core.util.exception.ServiceException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * <p>
 * 统一异常处理
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2018/10/16 10:16
 **/
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 所有异常报错
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public RestResult<Map<String, Object>> allExceptionHandler(Exception exception) {

        Map<String, Object> map = WebUtil.getRequestParam();
        log.error("统一异常处理 :参数 : {},异常信息 : {}", map, exception);
        return RestResult.fail(map, "操作失败!");
    }


    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public RestResult<Map<String, Object>> serviceException(ServiceException exception) {

        Map<String, Object> params = WebUtil.getRequestParam();
        log.error("系统异常 : ", exception);
        return RestResult.fail(params, exception.getMessage());
    }

    /**
     * 附件大小异常
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public RestResult<Map<String, Object>> maxUploadSizeException(MaxUploadSizeExceededException exception) {

        Map<String, Object> map = WebUtil.getRequestParam();
        String msg = "可以上传附件最大值 : " + exception.getMaxUploadSize() / 1024 / 1024 + "M";
        log.error("统一异常处理 :参数 : {},异常信息 : {}", map, exception);
        return RestResult.parameter(map, msg);
    }
}
