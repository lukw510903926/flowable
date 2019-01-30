package com.flowable.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flowable.util.LoginUser;
import com.flowable.util.WebUtil;

public class LoginFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Set<String> prefixIgnores = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) {

        String ignoresParam = filterConfig.getInitParameter("ignores");
        String[] ignoreArray = ignoresParam.split(",");
        prefixIgnores.addAll(Arrays.asList(ignoreArray));
        logger.info("loginFilter init -------------------");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        LoginUser loginUser = (LoginUser) httpRequest.getSession().getAttribute(WebUtil.LOGIN_USER);
        if (canIgnore(httpRequest)) {
            chain.doFilter(httpRequest, response);
            return;
        }
        logger.info("doFilter---------------------------------------------");
        if (loginUser == null) {
            String requestUri = httpRequest.getRequestURI();
            String queryStr = httpRequest.getQueryString();
            String contextPath = httpRequest.getContextPath();
            if (session.getAttribute(WebUtil.USER_URL) == null) {
                if (StringUtils.isNotBlank(queryStr)) {
                    requestUri += "?" + queryStr;
                }
                session.setAttribute(WebUtil.USER_URL, requestUri.substring(contextPath.length()));
            }
            httpResponse.sendRedirect(contextPath + "/login/login");
        } else {
            WebUtil.getLoginUser(httpRequest, httpResponse);
            httpRequest.getSession().setAttribute(WebUtil.LOGIN_USER, loginUser);
            chain.doFilter(httpRequest, response);
        }
    }

    private boolean canIgnore(HttpServletRequest request) {

        String url = request.getRequestURI();
        for (String ignore : prefixIgnores) {
            if (url.endsWith(ignore)) {
                return true;
            }
        }
        return "/login/login".equalsIgnoreCase(request.getServletPath());
    }

    @Override
    public void destroy() {
        logger.info("loginFilter destroy -------------------");
    }
}
