package com.flowable.oa.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author : lukew
 * @project : IDEA
 * @createTime : 2018/4/14 8:10
 * @email : 13507615840@163.com
 * @gitHub : https://github.com/lukw510903926
 * @description :
 */
public class WebUtil extends WebUtils {

    public final static String LOGIN_USER = "_SESSION_LOGIN_USER";

    public final static String SSO_TOKEN_COOKIE = "_SSO_TOKEN_COOKIE";

    public static LoginUser getLoginUser(HttpServletRequest request) {

        return (LoginUser) request.getSession().getAttribute(LOGIN_USER);
    }

    public static LoginUser getLoginUser() {

        return getLoginUser(getRequest());
    }

    /**
     * 获取请求的request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {

        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取请求的response
     *
     * @return
     */
    public static HttpServletResponse getResponse() {

        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static String getLoginUserId() {

        return Optional.ofNullable(getLoginUser()).map(loginUser -> loginUser.getId()).orElse(null);
    }

    public static void setSessionUser(HttpServletRequest request, LoginUser loginUser) {
        request.getSession().setAttribute(LOGIN_USER, loginUser);
    }

    public static void setToken(HttpServletRequest request, String token) {

        setSessionAttribute(request, SSO_TOKEN_COOKIE, token);
    }

    public static String getToken(HttpServletRequest request) {

        return (String) getSessionAttribute(request, SSO_TOKEN_COOKIE);
    }

    /**
     * 生成链接的URL
     *
     * @param href
     * @param ctx
     * @return
     */
    public static String getUrl(String href, String ctx) {

        String url = href;
        if (!Pattern.matches("^(http://|https://|/).*", url))
            url = ctx + '/' + url;
        return url;
    }

    /**
     * 是否Ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }

    /**
     * 获取请求参数
     *
     * @return
     */
    public static Map<String, Object> getRequestParam() {

        String paramName;
        HttpServletRequest request = getRequest();
        Map<String, Object> map = new HashMap<>();
        Enumeration<String> enumPks = request.getParameterNames();
        while (enumPks.hasMoreElements()) {
            paramName = enumPks.nextElement();
            map.put(paramName, request.getParameter(paramName));
        }
        return map;
    }
}
