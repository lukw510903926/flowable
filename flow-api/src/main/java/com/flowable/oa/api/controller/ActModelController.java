package com.flowable.oa.api.controller;

import com.flowable.oa.core.service.IProcessModelService;
import com.flowable.oa.core.util.RestResult;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 流程模型相关
 *
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午4:20:45
 * @description :
 */
@Slf4j
@Controller
@RequestMapping(value = "/act/model")
public class ActModelController {

    @Autowired
    private IProcessModelService processModelService;


    /**
     * 根据Model部署流程
     */
    @ResponseBody
    @RequestMapping(value = "deploy")
    public RestResult<Object> deploy(String id) {

        String deploy = processModelService.deploy(id);
        return RestResult.success("部署成功: " + deploy);
    }

    /**
     * 导出model的xml文件
     */
    @RequestMapping(value = "export")
    public void export(String id, HttpServletResponse response) {
        processModelService.export(id, response);
    }

    /**
     * 删除Model
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "delete")
    public RestResult<Object> delete(String id) {
        log.info("删除Model---delete");
        processModelService.delete(id);
        return RestResult.success();
    }
}
