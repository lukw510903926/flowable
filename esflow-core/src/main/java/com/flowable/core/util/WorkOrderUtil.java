package com.flowable.core.util;

import java.util.Date;

import com.flowable.common.utils.DateUtils;

/**
 * 
 * 
 * Title: workflowWeb <br>
 * Description: <br>
 * Copyright: eastcom Copyright (C) 2009 <br>
 * 
 * @author <a href="mailto:liyoujun@eastcom-sw.com">李友均</a><br>
 * @e-mail: liyoujun@eastcom-sw.com <br>
 * @version 1.0 <br>
 * @creatdate 2015年5月8日 下午3:23:18 <br>
 * 
 */
public class WorkOrderUtil {
	/**
	 * 根据类型转换
	 * 
	 * @param value
	 * @param type
	 * @return
	 */
	public static Object convObject(String value, String type) {
		
		if (value == null) {
			return null;
		}
		if (PageComponent.NUMBER.toString().equalsIgnoreCase(type)) {
			if (value.indexOf(".") == -1) {
				return Integer.parseInt(value);
			} else {
				return Double.parseDouble(value);
			}
		} else if (PageComponent.DATE.type().equalsIgnoreCase(type)
					|| PageComponent.TIME.type().equalsIgnoreCase(type)
					|| PageComponent.DATETIME.type().equalsIgnoreCase(type)) {
			return DateUtils.parseDate(value);
		} else if (PageComponent.BOOLEAN.type().equalsIgnoreCase(type)) {
			return "true".equalsIgnoreCase(value) || "1".equals(value);
		}
		return value;
	}

	public static enum PageComponent {
		
		TEXT("文本"), 
		
		TEXTAREA("文本域"),
		
		NUMBER("数字"),
		
		DATE("日期"), 
		
		TIME("时间"), 
		
		DATETIME("日期时间"),
		
		BOOLEAN("布尔"),
		
		MOBILE("手机号"), 
		
		EMAIL("邮箱");

		private String name;

		private PageComponent(String name) {
			this.name = name;
		}
		
		public String type(){
			return this.name;
		}
	}

	/**
	 * 生成工单号<br>
	 * 根据工单类型转换成拼音首字母-yyMMdd-xxxxx
	 * 
	 * @return
	 */
	public static String builWorkNumber(String workType) {
		String workNumber = null;
		if ("circuitDispatch".equals(workType)) {
			workNumber = "DLDD";
		} else {
			workNumber = "OTHER";
		}
		workNumber = workNumber + DateUtils.formatDate(new Date(), "yyMMdd");
		workNumber = workNumber + "-" + Math.round(Math.random() * 89999 + 10000);
		return workNumber;
	}
}
