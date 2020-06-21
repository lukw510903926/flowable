package com.flowable.oa.api.controller;

import com.flowable.oa.core.service.IProcessTaskAssigneeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :
 * @since : 2020-06-21 21:07
 */
@Validated
@RestController
@RequestMapping("process/task/assignee")
public class ProcessTaskAssigneeController {

    @Resource
    private IProcessTaskAssigneeService processTaskAssigneeService;
}
