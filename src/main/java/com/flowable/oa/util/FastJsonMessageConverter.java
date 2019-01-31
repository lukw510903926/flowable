package com.flowable.oa.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * @project : tykj-common
 * @createTime : 2017年5月17日 : 下午4:26:09
 * @author : lukewei
 * @description : fastJson消息转换器
 */
public class FastJsonMessageConverter {

	private final static String DATE_FORMAT = "YYYY-MM-dd HH:mm:ss";

	public static FastJsonHttpMessageConverter createConverter() {

		FastJsonHttpMessageConverter converter = createConverter(DATE_FORMAT);
		setMediaType(converter);
		return converter;
	} 
	
	public static FastJsonHttpMessageConverter createConverter(String dateFormat) {

		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = createFastJsonConfig(dateFormat);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		setMediaType(fastConverter);
		return fastConverter;
	}

	public static FastJsonHttpMessageConverter createConverter(SerializerFeature[] features) {

		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = createFastJsonConfig(features);
		fastJsonConfig.setDateFormat(DATE_FORMAT);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		setMediaType(fastConverter);
		return fastConverter;
	}

	public static FastJsonHttpMessageConverter createConverter(SerializerFeature[] features, String dateFormat) {

		FastJsonHttpMessageConverter fastConverter = createConverter(features);
		FastJsonConfig fastJsonConfig = createFastJsonConfig(features); 
		dateFormat = StringUtils.isEmpty(dateFormat) ? DATE_FORMAT : dateFormat;
		fastJsonConfig.setDateFormat(dateFormat);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		setMediaType(fastConverter);
		return fastConverter;
	}

	private static FastJsonConfig createFastJsonConfig(String dateFormat) {

		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		dateFormat = StringUtils.isEmpty(dateFormat) ? DATE_FORMAT : dateFormat;
		fastJsonConfig.setDateFormat(dateFormat);
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		return fastJsonConfig;
	}

	private static void setMediaType(FastJsonHttpMessageConverter fastConverter){

		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(MediaType.APPLICATION_JSON);
		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
		supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
		supportedMediaTypes.add(MediaType.APPLICATION_PDF);
		supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
		supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
		supportedMediaTypes.add(MediaType.APPLICATION_XML);
		supportedMediaTypes.add(MediaType.IMAGE_GIF);
		supportedMediaTypes.add(MediaType.IMAGE_JPEG);
		supportedMediaTypes.add(MediaType.IMAGE_PNG);
		supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
		supportedMediaTypes.add(MediaType.TEXT_HTML);
		supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
		supportedMediaTypes.add(MediaType.TEXT_PLAIN);
		supportedMediaTypes.add(MediaType.TEXT_XML);
		fastConverter.setSupportedMediaTypes(supportedMediaTypes);
	}

	private static FastJsonConfig createFastJsonConfig(SerializerFeature[] features) {

		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		if (ArrayUtils.isEmpty(features)) {
			fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		} else {
			fastJsonConfig.setSerializerFeatures(features);
		}
		return fastJsonConfig;
	}
}
