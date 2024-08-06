package com.zhang.core.convert.converter;

/**
 * 类型转换工厂
 *
 * @author zhang
 * @date 2024/7/13
 * @Description
 */
public interface ConverterFactory<S, R> {

	<T extends R> Converter<S, T> getConverter(Class<T> targetType);
}