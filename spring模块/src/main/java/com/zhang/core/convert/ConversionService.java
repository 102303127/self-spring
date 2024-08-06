package com.zhang.core.convert;

/**
 * 类型转换抽象接口
 *
 * @author zhang
 * @date 2024/7/13
 * @Description
 */
public interface ConversionService {

	boolean canConvert(Class<?> sourceType, Class<?> targetType);

	<T> T convert(Object source, Class<T> targetType);
}
