package com.flowable.oa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.flowable.oa.entity.ProcessVariable;
import com.flowable.oa.entity.ProcessVariableInstance;
import com.flowable.oa.service.IProcessVariableService;
import com.flowable.oa.service.IVariableInstanceService;

@Service
public class ProcessVariableServiceImplImpl extends BaseServiceImpl<ProcessVariable> implements IProcessVariableService {

	@Autowired
	private IVariableInstanceService variableInstanceService;

	@Override
	@Transactional
	public void addVariable(ProcessVariable... beans) {

		for (ProcessVariable bean : beans) {
			this.save(bean);
		}
	}

	@Override
	@Transactional
	public void updateVariable(ProcessVariable... beans) {

		for (ProcessVariable bean : beans) {
			if (bean.getId() == null) {
				continue;
			}
			this.updateNotNull(bean);
		}
	}

	@Override
	@Transactional
	public void deleteVariable(List<String> list) {

		if (CollectionUtils.isNotEmpty(list)) {
			list.forEach(variableId -> {
				if (StringUtils.isNotEmpty(variableId)) {
					ProcessVariableInstance instance = new ProcessVariableInstance();
					instance.setVariableId(variableId);
					this.variableInstanceService.deleteByModel(instance);
					this.deleteById(variableId);
				}
			});
		}
	}

	@Override
	public PageInfo<ProcessVariable> findProcessVariables(ProcessVariable variable, PageInfo<ProcessVariable> page) {

		if (page != null) {
			PageHelper.startPage(page.getPageNum(), page.getPageSize());
		}
		return new PageInfo<ProcessVariable>(this.findByModel(variable, false));
	}

	@Override
	public List<ProcessVariable> findProcessVariables(ProcessVariable variable) {

		return this.findProcessVariables(variable, null).getList();
	}

	@Override
	public void copyVariables(ProcessDefinition oldPdf, ProcessDefinition newPdf) throws Exception {

		if (oldPdf != null && newPdf != null) {
			int version_ = newPdf.getVersion();
			Map<String, String> refmap = new HashMap<String, String>();
			// 拷贝全局配置
			ProcessVariable example = new ProcessVariable();
			example.setProcessDefinitionId(oldPdf.getId());
			List<ProcessVariable> processValBeans = this.findByModel(example, false);
			List<ProcessVariable> processRefList = new ArrayList<ProcessVariable>();
			if (CollectionUtils.isNotEmpty(processValBeans)) {
				for (ProcessVariable valBean : processValBeans) {
					ProcessVariable processVar = valBean.clone();
					processVar.setProcessDefinitionId(newPdf.getId());
					processVar.setVersion(version_);
					save(processVar);
					refmap.put(valBean.getId(), processVar.getId());
					if (StringUtils.isNotBlank(processVar.getRefVariable())) {
						processRefList.add(processVar);
					}
				}
			}
			for (ProcessVariable tv : processRefList) {
				tv.setRefVariable(refmap.get(tv.getRefVariable()));
				this.updateNotNull(tv);
			}
		}
	}
}
