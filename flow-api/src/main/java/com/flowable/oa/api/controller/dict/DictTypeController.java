package com.flowable.oa.api.controller.dict;

import com.flowable.oa.core.entity.dict.DictType;
import com.flowable.oa.core.service.dict.IDictTypeService;
import com.flowable.oa.core.util.DataGrid;
import com.flowable.oa.core.util.RestResult;
import com.github.pagehelper.PageInfo;
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
@RequestMapping("dictType")
public class DictTypeController {

    @Autowired
    private IDictTypeService dictTypeService;

    @RequestMapping("save")
    public RestResult<Object> save(DictType dictType) {

        this.dictTypeService.saveOrUpdate(dictType);
        return RestResult.success();
    }


    @RequestMapping("update")
    public RestResult<Object> update(DictType dictType) {

        if (null != dictType.getId()) {
            return RestResult.fail(null, "id不可为空");
        }
        this.dictTypeService.saveOrUpdate(dictType);
        return RestResult.success();
    }

    @RequestMapping("delete")
    public RestResult<Object> delete(@RequestBody List<Long> list) {

        this.dictTypeService.delete(list);
        return RestResult.success();
    }

    @RequestMapping("get/{typeId}")
    public RestResult<DictType> getEdit(@PathVariable("typeId") Integer typeId) {
        return RestResult.success(this.dictTypeService.selectByKey(typeId));
    }

    @RequestMapping("list")
    public RestResult<DataGrid<DictType>> findDictType(PageInfo<DictType> page, DictType dictType) {

        PageInfo<DictType> helper = this.dictTypeService.findByModel(page, dictType, true);
        DataGrid<DictType> grid = new DataGrid<>();
        grid.setRows(helper.getList());
        grid.setTotal(helper.getTotal());
        return RestResult.success(grid);
    }
}
