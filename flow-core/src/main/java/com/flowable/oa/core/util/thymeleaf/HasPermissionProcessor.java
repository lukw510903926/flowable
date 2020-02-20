package com.flowable.oa.core.util.thymeleaf;

import com.flowable.oa.core.util.WebUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.standard.processor.AbstractStandardConditionalVisibilityTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :
 * @since : 2020/2/20 5:44 下午
 */
@Slf4j
public class HasPermissionProcessor extends AbstractStandardConditionalVisibilityTagProcessor {

    private static final int PRECEDENCE = 300;

    private static final String ATTR_NAME = "permission";

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
        if (log.isDebugEnabled()) {
            log.debug("urls : {} .value : {}", urls, attributeValue);
        }
        return urls.contains(attributeValue);
    }
}
