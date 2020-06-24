package com.flowable.oa.controller;

import com.flowable.oa.core.service.IProcessEngineService;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProcessController {

    @Autowired
    private IProcessEngineService processEngineService;

    /**
     * 模型管理视图
     *
     * @return
     */
    @RequestMapping("model/list")
    public String modelView() {
        return "modules/process/model_list";
    }

    /**
     * 创建模型视图
     *
     * @return
     */
    @RequestMapping("model/create")
    public String createModelView() {
        return "modules/process/model_create";
    }

    /**
     * 流程管理视图
     *
     * @return
     */
    @GetMapping("process/list")
    public String processView() {
        return "modules/process/process_list";
    }

    /**
     * 发布流程视图
     *
     * @return
     */
    @GetMapping("process/deploy")
    public String deployProcessView() {
        return "modules/process/process_deploy";
    }

    /**
     * 部署流程 - 保存
     *
     * @return
     */
    @PostMapping("process/deploy")
    public String deployProcess(MultipartFile file, RedirectAttributes redirectAttributes, Model model) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            redirectAttributes.addFlashAttribute("message", "请选择要部署的流程文件");
        } else {
            String message = processEngineService.deploy(file);
            redirectAttributes.addFlashAttribute("message", message);
            model.addAttribute("message", "success");
        }
        return "modules/process/process_deploy";
    }

    /**
     * 设置流程变量视图
     *
     * @return
     */
    @RequestMapping("process/variable")
    public String setProcessVariableView(@RequestParam Map<String, Object> params, Model model) {
        model.addAllAttributes(params);
        return "modules/process/variable_list";
    }

    @RequestMapping("process/variable/edit")
    public String setProcessVariableEdit(@RequestParam Map<String, Object> params, Model model) {
        model.addAllAttributes(params);
        return "modules/process/variable_edit";
    }

    /**
     * 设置流程任务视图
     *
     * @return
     */
    @RequestMapping("process/task/list")
    public String setTaskView(@RequestParam Map<String, Object> params, Model model) {
        model.addAllAttributes(params);
        return "modules/process/task_list";
    }
}
