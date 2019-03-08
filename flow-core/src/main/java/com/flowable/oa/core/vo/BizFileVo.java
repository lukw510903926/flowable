package com.flowable.oa.core.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * <p>附件
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/9 3:05
 **/
@Data
public class BizFileVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;

	private String bizId;

	private String taskName;

	private String taskId;

	private String name;

	private String fileCatalog;
}