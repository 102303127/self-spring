package com.zhang.core.convert.support;


import com.zhang.core.convert.converter.ConverterRegistry;

/**
 * @author zhang
 * @date 2024/7/3
 * @Description
 */
public class DefaultConversionService extends GenericConversionService {

	public DefaultConversionService() {
		addDefaultConverters(this);
	}

	public static void addDefaultConverters(ConverterRegistry converterRegistry) {
		converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
		//TODO 添加其他ConverterFactory
	}
}
