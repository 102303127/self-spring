package com.zhang.core.convert.converter;

/**
 * 类型转换器注册接口
 *
 * @author zhang
 * @date 2024/7/13
 * @Description
 */
public interface ConverterRegistry {

	void addConverter(Converter<?, ?> converter);

	void addConverterFactory(ConverterFactory<?, ?> converterFactory);

	void addConverter(GenericConverter converter);
}
