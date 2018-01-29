package com.flowable.web.controller.dict;

import com.flowable.common.utils.DataGrid;
import com.flowable.common.utils.Json;
import com.flowable.common.utils.PageHelper;
import com.flowable.common.utils.ReflectionUtils;
import com.flowable.core.bean.dict.DictType;
import com.flowable.core.service.dict.IDictTypeService;
import com.flowable.core.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/dicts")
    public String tables(HttpServletRequest request) {

        return "/dict/dict_list";
    }

    @ResponseBody
    @RequestMapping("save")
    public Json save(HttpServletRequest request, DictType dictType) {

        try {
            WebUtil.getLoginUser(request);
            this.dictTypeService.saveOrUpdate(dictType);
        } catch (Exception e) {
            logger.error("保存失败 : {}", e);
            return new Json("保存失败:" + e.getLocalizedMessage(), false);
        }
        return new Json(null, true);
    }


    @ResponseBody
    @RequestMapping("update")
    public Json update(HttpServletRequest request, DictType dictType) {

        if (StringUtils.isBlank(dictType.getId())) {
            new Json("id不可为空", false);
        }
        try {
            WebUtil.getLoginUser(request);
            this.dictTypeService.saveOrUpdate(dictType);
        } catch (Exception e) {
            logger.error("更新失败 : {}", e);
            return new Json("更新失败:" + e.getLocalizedMessage(), false);
        }
        return new Json(null, true);
    }

    @ResponseBody
    @RequestMapping("delete")
    public Json delete(HttpServletRequest request, DictType dictType) {

        try {
            WebUtil.getLoginUser(request);
            this.dictTypeService.delete(dictType);
        } catch (Exception e) {
            logger.error("删除失败 : {}", e);
            return new Json("删除失败:" + e.getLocalizedMessage(), false);
        }
        return new Json(null, true);
    }
    
    @ResponseBody
    @RequestMapping("get/{typeId}")
    public DictType getEdit(@PathVariable("typeId")String typeId) {
       return  this.dictTypeService.get(typeId);
    }

    @ResponseBody
    @RequestMapping("list")
    public DataGrid findDictType(PageHelper<DictType> page, DictType dictType) {

        DataGrid dataGrid = new DataGrid();
        PageHelper<DictType> result = this.dictTypeService.findByParams(ReflectionUtils.beanToMap(dictType),page,  true);
        dataGrid.setRows(result.getList());
        dataGrid.setTotal(result.getCount());
        return dataGrid;
    }
}
