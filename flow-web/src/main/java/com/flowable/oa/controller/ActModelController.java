package com.flowable.oa.controller;

import com.flowable.oa.core.service.IProcessModelService;
import com.flowable.oa.core.util.RestResult;
import org.flowable.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * 流程模型相关
 *
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午4:20:45
 * @description :
 */
@Controller
@RequestMapping(value = "/act/model")
public class ActModelController {

    @Autowired
    private IProcessModelService processModelService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 创建模型
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(String name, String key, String description, String category,
                         org.springframework.ui.Model model) {
        try {
            Model modelData = processModelService.create(name, key, description, category);
            model.addAttribute("message", "success");
            model.addAttribute("modelId", modelData.getId());
        } catch (Exception e) {
            logger.error("创建模型失败：", e);
        }
        return "modules/process/act/actModelCreate";
    }

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
     * 更新Model分类
     */
    @RequestMapping(value = "updateCategory")
    public String updateCategory(String id, String category, RedirectAttributes redirectAttributes) {
        processModelService.updateCategory(id, category);
        redirectAttributes.addFlashAttribute("message", "设置成功，模块ID=" + id);
        return "redirect:/act/model";
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
        logger.info("删除Model---delete");
        processModelService.delete(id);
        return RestResult.success();
    }
}
