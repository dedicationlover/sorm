package com.sorm.utils;

import java.lang.reflect.Method;

/**
 * ��װ�˷��䳣�õĲ���
 * @author Administrator
 *
 */
public class ReflectUtils {

	/**
	 * ����obj�����Ӧ����fieldName��get����
	 * @param fildName ������
	 * @param obj ��������
	 * @return ��ȡ���Ե�ֵ
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
	 * �������Ե�set����
	 * @param obj ��������
	 * @param columnName ������
	 * @param columnValue ����ֵ
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
