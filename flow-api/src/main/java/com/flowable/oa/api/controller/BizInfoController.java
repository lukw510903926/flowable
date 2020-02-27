package com.flowable.oa.api.controller;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.service.IBizInfoService;
import com.flowable.oa.core.service.IProcessEngineService;
import com.flowable.oa.core.service.IProcessExecuteService;
import com.flowable.oa.core.util.Constants;
import com.flowable.oa.core.util.PageUtil;
import com.flowable.oa.core.util.RestResult;
import com.flowable.oa.core.vo.BizInfoVo;
import com.github.pagehelper.PageInfo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public RestResult<List<String>> getProcessStatus(ProcessDefinitionEntityImpl processDefinition) {

        List<ProcessDefinition> processList = processEngineService.findProcessDefinition(null);
        return RestResult.success(this.getProcessStatus(processList, processDefinition));
    }

    private List<String> getProcessStatus(List<ProcessDefinition> list, ProcessDefinition processDefinition) {

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
            list = processEngineService.findProcessDefinition(processDefinition);
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
