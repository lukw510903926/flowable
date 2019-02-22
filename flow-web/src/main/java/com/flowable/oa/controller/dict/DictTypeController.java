package com.flowable.oa.controller.dict;

import com.flowable.oa.core.entity.dict.DictType;
import com.flowable.oa.core.service.dict.IDictTypeService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午11:11
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
    public RestResult<Object> save(DictType dictType) {

        this.dictTypeService.saveOrUpdate(dictType);
        return RestResult.success();
    }


    @ResponseBody
    @RequestMapping("update")
    public RestResult<Object> update(DictType dictType) {

        if (StringUtils.isBlank(dictType.getId())) {
           return RestResult.fail(null,"id不可为空");
        }
        this.dictTypeService.saveOrUpdate(dictType);
        return RestResult.success();
    }

    @ResponseBody
    @RequestMapping("delete")
    public RestResult<Object> delete(@RequestBody List<String> list) {

        this.dictTypeService.delete(list);
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
