package com.flowable.web.controller.dict;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.flowable.entity.dict.DictType;
import com.flowable.service.dict.IDictTypeService;
import com.flowable.util.DataGrid;
import com.flowable.util.Json;
import com.flowable.util.PageHelper;
import com.flowable.util.ReflectionUtils;
import com.flowable.util.WebUtil;

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
	public String tables(HttpServletRequest request) {

		return "/dict/dict_list";
	}

	@ResponseBody
	@RequestMapping("save")
	public Json save(HttpServletRequest request, DictType dictType) {

		WebUtil.getLoginUser(request);
		this.dictTypeService.saveOrUpdate(dictType);
		return new Json(null, true);
	}

	@ResponseBody
	@RequestMapping("update")
	public Json update(HttpServletRequest request, DictType dictType) {

		if (StringUtils.isBlank(dictType.getId())) {
			new Json("id不可为空", false);
		}
		WebUtil.getLoginUser(request);
		this.dictTypeService.saveOrUpdate(dictType);
		return new Json(null, true);
	}

	@ResponseBody
	@RequestMapping("delete")
	public Json delete(HttpServletRequest request, DictType dictType) {

		WebUtil.getLoginUser(request);
		this.dictTypeService.delete(dictType);
		return new Json(null, true);
	}

	@ResponseBody
	@RequestMapping("get/{typeId}")
	public DictType getEdit(@PathVariable("typeId") String typeId) {
		return this.dictTypeService.get(typeId);
	}

	@ResponseBody
	@RequestMapping("list")
	public DataGrid findDictType(PageHelper<DictType> page, DictType dictType) {

		DataGrid dataGrid = new DataGrid();
		PageHelper<DictType> result = this.dictTypeService.findByParams(ReflectionUtils.beanToMap(dictType), page,
				true);
		dataGrid.setRows(result.getList());
		dataGrid.setTotal(result.getCount());
		return dataGrid;
	}
}
