package com.flowable.oa.controller;

import com.flowable.oa.core.service.IProcessTaskAssigneeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :
 * @since : 2020-06-21 21:08
 */
@Controller
@RequestMapping("process/task/assignee")
public class ProcessTaskAssigneeController {

    @Resource
    private IProcessTaskAssigneeService processTaskAssigneeService;
}
