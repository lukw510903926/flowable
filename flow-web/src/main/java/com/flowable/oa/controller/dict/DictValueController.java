package com.flowable.oa.controller.dict;

import com.flowable.oa.core.entity.dict.DictValue;
import com.flowable.oa.core.service.dict.IDictValueService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("dictValue")
public class DictValueController {

    @Autowired
    private IDictValueService dictValueService;

    @RequestMapping("/list/{typeId}")
    public String dictValues(@PathVariable("typeId") String typeId, Model model) {

        model.addAttribute("typeId", typeId);
        return "modules/dict/value_list";
    }

    @ResponseBody
    @RequestMapping("/value/{valueId}")
    public DictValue getById(@PathVariable("valueId") Integer valueId) {

        return this.dictValueService.getById(valueId);
    }

    @ResponseBody
    @RequestMapping("save")
    public RestResult<Object> save(DictValue dictValue) {

        this.dictValueService.saveOrUpdate(dictValue);
        return RestResult.success();
    }

    @ResponseBody
    @RequestMapping("update")
    public RestResult<Object> update(DictValue dictValue) {

        if (null != dictValue.getId()) {
            return RestResult.fail(dictValue, "id不可为空");
        }
        this.dictValueService.saveOrUpdate(dictValue);
        return RestResult.success();
    }

    @ResponseBody
    @RequestMapping("delete")
    public RestResult<Object> delete(@RequestBody List<Integer> list) {

        this.dictValueService.deleteByIds(list);
        return RestResult.success();
    }

    @ResponseBody
    @RequestMapping("list")
    public DataGrid<DictValue> findDictValue(PageInfo<DictValue> page, DictValue dictValue) {

        PageInfo<DictValue> helper = this.dictValueService.findByModel(page, dictValue, false);
        DataGrid<DictValue> grid = new DataGrid<>();
        grid.setRows(helper.getList());
        grid.setTotal(helper.getTotal());
        return grid;
    }
}
