package com.sorm.core;

/**
 * ����java�������ͺ����ݿ��������͵��໥ת��
 * @author Administrator
 *
 */
public interface TypeConvertor {

	/**
	 * �����ݿ�����ת��Ϊjava����������
	 * @param columnType ���ݿ��ֶε���������
	 * @return java����������
	 */
	public String databaseType2JavaType(String columnType);
	
	/**
	 * ��java��������ת�������ݿ���������
	 * @param javaType java��������
	 * @return ���ݿ�����
	 */
	public String javaType2DatabaseType(String javaType);
}
