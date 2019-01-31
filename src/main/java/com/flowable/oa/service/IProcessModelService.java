package com.flowable.oa.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;
import com.github.pagehelper.PageInfo;
import org.flowable.engine.repository.Model;


public interface IProcessModelService {

	/**
	 * 流程模型列表
	 */
	PageInfo<Model> modelList(PageInfo<Model> page, String category);

	/**
	 * 创建模型
	 * @throws UnsupportedEncodingException 
	 */
	Model create(String name, String key, String description, String category) throws UnsupportedEncodingException;

	/**
	 * 根据Model部署流程
	 */
	String deploy(String id);

	/**
	 * 导出model的xml文件
	 * @throws IOException 
	 */
	void export(String id, HttpServletResponse response);

	/**
	 * 更新Model分类
	 */
	void updateCategory(String id, String category);

	/**
	 * 删除模型
	 * @param id
	 * @return
	 */
	void delete(String id);
}
