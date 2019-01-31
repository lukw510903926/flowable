package com.flowable.oa.controller.dict;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.flowable.oa.util.Json;
import com.flowable.oa.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/dicts")
    public String tables(HttpServletRequest request) {

        return "modules/dict/dict_list";
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
       return  this.dictTypeService.selectByKey(typeId);
    }

    @ResponseBody
    @RequestMapping("list")
    public PageInfo<DictType> findDictType(PageInfo<DictType> page, DictType dictType) {

        return this.dictTypeService.findByModel(page,dictType, true);
    }
}
