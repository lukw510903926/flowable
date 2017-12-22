package com.flowable.core.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonProcessingException;
import org.flowable.engine.repository.Model;

import com.flowable.common.utils.PageHelper;

public interface IProcessModelService {

	/**
	 * 流程模型列表
	 */
	public PageHelper<Model> modelList(PageHelper<Model> page, String category);

	/**
	 * 创建模型
	 * @throws UnsupportedEncodingException 
	 */
	public Model create(String name, String key, String description, String category) throws UnsupportedEncodingException;

	/**
	 * 根据Model部署流程
	 */
	public String deploy(String id);

	/**
	 * 导出model的xml文件
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	public void export(String id, HttpServletResponse response);

	/**
	 * 更新Model分类
	 */
	public void updateCategory(String id, String category);

	/**
	 * 删除模型
	 * @param id
	 * @return
	 */
	public void delete(String id);
}
