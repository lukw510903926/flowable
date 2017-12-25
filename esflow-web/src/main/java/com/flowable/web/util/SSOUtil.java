package com.flowable.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.flowable.common.utils.LoginUser;
import com.flowable.core.util.WebUtil;

/**
 * 
 * @author 26223
 *
 */
public class SSOUtil {
	
	public static final String CACHE_SESSIONINFO = "sessionInfo";
	
	private static Logger logger = Logger.getLogger("SSOUtil");

	public static LoginUser getInstance(HttpServletRequest request, HttpServletResponse response) {
		
		LoginUser user = (LoginUser) request.getSession().getAttribute(WebUtil.LOGIN_USER);
		if (user == null) {
			try {
				request.getSession().invalidate();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("error ",e);
			}
		}
		return user;
	}

	public static LoginUser getUser(HttpServletRequest request, HttpServletResponse response) {
		LoginUser user = (LoginUser) request.getSession().getAttribute(WebUtil.LOGIN_USER);
		return user;
	}

	public static String getUserName(HttpServletRequest request, HttpServletResponse response) {
		LoginUser user = (LoginUser) request.getSession().getAttribute(WebUtil.LOGIN_USER);
		String userName = user == null ? null : user.getUsername();
		return userName;
	}
	
	// ============== SSO Cache ==============
	private static Map<String, Object> cacheMap;

	public static Object getCache(String key) {
		return getCache(key, null);
	}

	public static Object getCache(String key, Object defaultValue) {
		Object obj = getCacheMap().get(key);
		return obj == null ? defaultValue : obj;
	}

	public static void putCache(String key, Object value) {
		getCacheMap().put(key, value);
	}

	public static void removeCache(String key) {
		getCacheMap().remove(key);
	}

	public static Map<String, Object> getCacheMap() {
		if (cacheMap == null) {
			cacheMap = new HashMap<String, Object>();
		}
		return cacheMap;
	}
	
	private static Map<String, List<String>> constructMapByList2(List<String> list) {
		String[] array;
		List<String> l;
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String str : list) {
			array = str.split(":");
			if (!map.containsKey(array[0])) {
				l = new ArrayList<String>();
				map.put(array[0], l);
			} else {
				l = map.get(array[0]);
			}
			l.add(array[1]);
		}
		return map;
	}

}