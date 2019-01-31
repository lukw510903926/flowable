package com.flowable.oa.util.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2018-02-07 14:17
 **/
public class HasPermissionDialect extends AbstractProcessorDialect {

	private static String PREFIX = "has";

	public static final String NAME = "Standard";

	public static final int PROCESSOR_PRECEDENCE = 1000;

	public HasPermissionDialect() {
		super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		final Set<IProcessor> processors = new HashSet<IProcessor>();
		processors.add(new HasPermissionProcessor(TemplateMode.HTML,dialectPrefix));
		return processors;
	}
}