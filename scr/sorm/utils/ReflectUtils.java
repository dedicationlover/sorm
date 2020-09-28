package com.sorm.utils;

import java.lang.reflect.Method;

/**
 * 封装了反射常用的操作
 * @author Administrator
 *
 */
public class ReflectUtils {

	/**
	 * 调用obj对象对应属性fieldName的get方法
	 * @param fildName 属性名
	 * @param obj 被调对象
	 * @return 获取属性的值
	 */
	public static Object invokeGet(String fildName, Object obj){
		try {
			Class c = obj.getClass();
			Method m = c.getDeclaredMethod("get" + StringUtils.firstChar2UpperCase(fildName), null);
			return m.invoke(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 调用属性的set方法
	 * @param obj 被调对象
	 * @param columnName 属性名
	 * @param columnValue 属性值
	 */
	public static void invokeSet( Object obj, String columnName, Object columnValue){
		try {
			Class c = obj.getClass();
			Method m = c.getDeclaredMethod("set" + StringUtils.firstChar2UpperCase(columnName), columnValue.getClass());
			m.invoke(obj, columnValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
