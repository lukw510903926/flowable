package com.flowable.oa.api.controller.dict;

import com.flowable.oa.core.entity.dict.DictValue;
import com.flowable.oa.core.service.dict.IDictValueService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.github.pagehelper.PageInfo;
import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午11:11
 **/
@RestController
@RequestMapping("dictValue")
public class DictValueController {

    @Autowired
    private IDictValueService dictValueService;

    @RequestMapping("/value/{valueId}")
    public RestResult<DictValue> getById(@PathVariable("valueId") Integer valueId) {

        return RestResult.success(this.dictValueService.getById(valueId));
    }

    @RequestMapping("save")
    public RestResult<Object> save(DictValue dictValue) {

        this.dictValueService.saveOrUpdate(dictValue);
        return RestResult.success();
    }

    @RequestMapping("update")
    public RestResult<Object> update(DictValue dictValue) {

        if (dictValue.getId() != null) {
            return RestResult.fail(dictValue, "id不可为空");
        }
        this.dictValueService.saveOrUpdate(dictValue);
        return RestResult.success();
    }

    @RequestMapping("delete")
    public RestResult<Object> delete(@RequestBody List<Serializable> list) {

        this.dictValueService.deleteByIds(list);
        return RestResult.success();
    }

    @RequestMapping("list")
    public RestResult<DataGrid<DictValue>> findDictValue(PageInfo<DictValue> page, DictValue dictValue) {

        PageInfo<DictValue> helper = this.dictValueService.findByModel(page, dictValue, false);
        DataGrid<DictValue> grid = new DataGrid<>();
        grid.setRows(helper.getList());
        grid.setTotal(helper.getTotal());
        return RestResult.success(grid);
    }
}
