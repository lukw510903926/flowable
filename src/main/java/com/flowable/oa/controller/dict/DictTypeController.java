package com.flowable.oa.controller.dict;

import javax.servlet.http.HttpServletRequest;

import com.flowable.oa.util.DataGrid;
import com.flowable.oa.util.RestResult;
import com.github.pagehelper.PageInfo;
import com.flowable.oa.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flowable.oa.entity.dict.DictType;
import com.flowable.oa.service.dict.IDictTypeService;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2017-12-06 14:43
 **/
@Controller
@RequestMapping("dictType")
public class DictTypeController {

    @Autowired
    private IDictTypeService dictTypeService;

    @RequestMapping("/dicts")
    public String tables() {

        return "modules/dict/dict_list";
    }

    @ResponseBody
    @RequestMapping("save")
    public RestResult<Object> save(HttpServletRequest request, DictType dictType) {

        WebUtil.getLoginUser(request);
        this.dictTypeService.saveOrUpdate(dictType);
        return RestResult.success();
    }


    @ResponseBody
    @RequestMapping("update")
    public RestResult<Object> update(HttpServletRequest request, DictType dictType) {

        if (StringUtils.isBlank(dictType.getId())) {
           return RestResult.fail(null,"id不可为空");
        }
        WebUtil.getLoginUser(request);
        this.dictTypeService.saveOrUpdate(dictType);
        return RestResult.success();
    }

    @ResponseBody
    @RequestMapping("delete")
    public RestResult<Object> delete(HttpServletRequest request, DictType dictType) {

        WebUtil.getLoginUser(request);
        this.dictTypeService.delete(dictType);
        return RestResult.success();
    }

    @ResponseBody
    @RequestMapping("get/{typeId}")
    public DictType getEdit(@PathVariable("typeId") String typeId) {
        return this.dictTypeService.selectByKey(typeId);
    }

    @ResponseBody
    @RequestMapping("list")
    public DataGrid<DictType> findDictType(PageInfo<DictType> page, DictType dictType) {

        PageInfo<DictType> helper = this.dictTypeService.findByModel(page, dictType, true);
        DataGrid<DictType> grid = new DataGrid<>();
        grid.setRows(helper.getList());
        grid.setTotal(helper.getTotal());
        return grid;
    }
}
