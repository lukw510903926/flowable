package com.flowable.oa.controller.dict;

import javax.servlet.http.HttpServletRequest;

import com.flowable.oa.util.RestResult;
import com.github.pagehelper.PageInfo;
import com.flowable.oa.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.flowable.oa.entity.dict.DictValue;
import com.flowable.oa.service.dict.IDictValueService;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2017-12-06 14:43
 **/
@Controller
@RequestMapping("dictValue")
public class DictValueController {

    @Autowired
    private IDictValueService dictValueService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/list/{typeId}")
    public String dictValues(@PathVariable("typeId") String typeId, Model model) {

        model.addAttribute("typeId", typeId);
        return "modules/dict/value_list";
    }

    @ResponseBody
    @RequestMapping("/value/{valueId}")
    public DictValue getById(@PathVariable("valueId") String valueId) {

        return this.dictValueService.getById(valueId);
    }

    @ResponseBody
    @RequestMapping("save")
    public RestResult<Object> save(HttpServletRequest request, DictValue dictValue) {

        WebUtil.getLoginUser(request);
        this.dictValueService.saveOrUpdate(dictValue);
        return RestResult.success();
    }

    @ResponseBody
    @RequestMapping("update")
    public RestResult<Object> update(HttpServletRequest request, DictValue dictValue) {

        if (StringUtils.isBlank(dictValue.getId())) {
            return RestResult.fail(dictValue, "id不可为空");
        }
        WebUtil.getLoginUser(request);
        this.dictValueService.saveOrUpdate(dictValue);
        return RestResult.success();
    }

    @ResponseBody
    @RequestMapping("delete")
    public RestResult<Object> delete(HttpServletRequest request, DictValue dictValue) {

        WebUtil.getLoginUser(request);
        this.dictValueService.deleteById(dictValue.getId());
        return RestResult.success();
    }

    @ResponseBody
    @RequestMapping("list")
    public PageInfo<DictValue> findDictValue(PageInfo<DictValue> page, DictValue dictValue) {

        return this.dictValueService.findByModel(page, dictValue, false);
    }
}
