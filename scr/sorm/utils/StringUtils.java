package com.sorm.utils;

/**
 * ��װ���ַ������õĲ���
 * @author Administrator
 *
 */
public class StringUtils {

	/**
	 * ��Ŀ���ַ�������ĸ��Ϊ��д
	 * @param str Ŀ���ַ���
	 * @return Ŀ���ַ�������ĸ��Ϊ��д
	 */
	public static String firstChar2UpperCase(String str){
		return str.toUpperCase().substring(0, 1) + str.substring(1);
	}
	
}
