package com.flowable.oa.util.thymeleaf;

import java.util.List;

import com.flowable.oa.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.standard.processor.AbstractStandardConditionalVisibilityTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

public class HasPermissionProcessor extends AbstractStandardConditionalVisibilityTagProcessor {

	public static final int PRECEDENCE = 300;

	public static final String ATTR_NAME = "permission";

	private Logger logger = LoggerFactory.getLogger(HasPermissionProcessor.class);

	public HasPermissionProcessor(final TemplateMode templateMode, final String dialectPrefix) {
		super(templateMode, dialectPrefix, ATTR_NAME, PRECEDENCE);
	}

	@Override
	protected boolean isVisible(final ITemplateContext context, final IProcessableElementTag tag,
			final AttributeName attributeName, final String attributeValue) {

		if (StringUtils.isEmpty(attributeValue)) {
			throw new RuntimeException("参数不可为空!");
		}
		List<String> urls = WebUtil.getLoginUser().getUrls();
		if (logger.isDebugEnabled()) {
			logger.debug("urls : {} .value : {}", urls, attributeValue);
		}
		return urls.contains(attributeValue);
	}
}
