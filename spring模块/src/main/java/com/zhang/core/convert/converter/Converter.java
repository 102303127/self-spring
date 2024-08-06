package com.zhang.core.convert.converter;

/**
 * 类型转换抽象接口
 *
 * @author zhang
 * @date 2024/7/13
 * @Description
 */
public interface Converter<S, T> {

	/**
	 * 类型转换
	 */
	T convert(S source);
}
