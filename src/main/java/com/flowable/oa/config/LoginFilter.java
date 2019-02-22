package com.flowable.oa.config;

import com.flowable.oa.util.LoginUser;
import com.flowable.oa.util.WebUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-15 下午11:11
 **/
@WebFilter(urlPatterns = "/*", displayName = "loginFilter")
public class LoginFilter implements Filter {

    private String[] ignoreUrl = new String[]{"js", "css", "font-awesome", "images", "modules", "/login/login"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LoginUser loginUser = WebUtil.getLoginUser();
        StringBuffer requestURL = request.getRequestURL();
        if (pass(requestURL.toString())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login/login");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }

    private boolean pass(String requestUrl) {

        for (String path : ignoreUrl) {
            if (requestUrl.contains(path)) {
                return true;
            }
        }
        return false;
    }
}
