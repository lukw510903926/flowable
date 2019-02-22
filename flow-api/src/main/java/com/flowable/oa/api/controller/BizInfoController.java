package com.flowable.oa.api.controller;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.service.IBizInfoService;
import com.flowable.oa.core.util.PageInfoUtil;
import com.flowable.oa.core.util.RestResult;
import com.flowable.oa.core.vo.BizInfoVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/2/22 22:51
 **/
@RestController
@RequestMapping("/bizInfo")
public class BizInfoController {

    @Autowired
    private IBizInfoService bizInfoService;

    @PostMapping("/list")
    public RestResult<PageInfo<BizInfo>> list(@RequestBody BizInfoVo bizInfoVo) {

        return RestResult.success(this.bizInfoService.findBizInfo(new HashMap<>(), PageInfoUtil.getPage(bizInfoVo)));
    }
}
