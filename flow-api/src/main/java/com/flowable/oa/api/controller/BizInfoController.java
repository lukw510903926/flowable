package com.flowable.oa.api.controller;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.service.IBizInfoService;
import com.flowable.oa.core.service.IProcessEngineService;
import com.flowable.oa.core.service.IProcessExecuteService;
import com.flowable.oa.core.util.Constants;
import com.flowable.oa.core.util.PageUtil;
import com.flowable.oa.core.util.RestResult;
import com.flowable.oa.core.vo.BizInfoVo;
import com.flowable.oa.core.vo.ProcessDefinitionEntityVo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午10:56
 **/
@RestController
@RequestMapping("/bizInfo")
public class BizInfoController {

    @Autowired
    private IProcessEngineService processEngineService;

    @Autowired
    private IProcessExecuteService processExecuteService;

    @Autowired
    private IBizInfoService bizInfoService;

    @RequestMapping("/biz/process/status")
    public RestResult<List<String>> getProcessStatus(ProcessDefinitionEntityVo processDefinition) {

        PageInfo<ProcessDefinitionEntityVo> processList = processEngineService.processList(processDefinition);
        return RestResult.success(this.getProcessStatus(processList.getList(), processDefinition));
    }

    private List<String> getProcessStatus(List<ProcessDefinitionEntityVo> list, ProcessDefinitionEntityVo processDefinition) {

        List<String> status = new ArrayList<>();
        status.add(Constants.BIZ_TEMP);
        status.add(Constants.BIZ_NEW);
        Set<String> sets = new HashSet<>();
        if (StringUtils.isBlank(processDefinition.getName())) {
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(entity -> sets.addAll(processEngineService.loadProcessStatus(entity.getId())));
            }
            status.addAll(sets);
            status.add(Constants.BIZ_END);
        } else {
            list = processEngineService.processList(processDefinition).getList();
            if (CollectionUtils.isNotEmpty(list)) {
                sets.addAll(this.processEngineService.loadProcessStatus(list.get(0).getId()));
            }
        }
        status.add(Constants.BIZ_END);
        return status;
    }

    @RequestMapping("detail/{bizId}")
    public RestResult<Map<String, Object>> getDraftBiz(@PathVariable("bizId") Long bizId) {
        return RestResult.success(processExecuteService.queryWorkOrder(bizId));
    }

    @PostMapping("/list")
    public RestResult<PageInfo<BizInfo>> list(@RequestBody BizInfoVo bizInfoVo) {

        return RestResult.success(this.bizInfoService.findBizInfo(bizInfoVo, PageUtil.getPage(bizInfoVo)));
    }
}
