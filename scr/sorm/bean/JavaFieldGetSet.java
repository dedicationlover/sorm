package com.sorm.bean;

/**
 * ��װ��java���Ժ�get��set������Դ����
 * @author Administrator
 *
 */
public class JavaFieldGetSet {
	/**
	 * ���Ե�Դ����Ϣ����:private int id;
	 */
	private String fieldInfo;
	/**
	 * get������Դ����Ϣ����: public int get(){...}
	 */
	private String getInfo;
	/**
	 * set������Դ����Ϣ����: public void set(int id){...}
	 */
	private String setInfo;
	
	public String toString(){
		System.out.println(fieldInfo);
		System.out.println(getInfo);
		System.out.println(setInfo);
		return super.toString();
	}
	
	// setter and getter methods
	
	public String getFieldInfo() {
		return fieldInfo;
	}
	public void setFieldInfo(String fieldInfo) {
		this.fieldInfo = fieldInfo;
	}
	public String getGetInfo() {
		return getInfo;
	}
	public void setGetInfo(String getInfo) {
		this.getInfo = getInfo;
	}
	public String getSetInfo() {
		return setInfo;
	}
	public void setSetInfo(String setInfo) {
		this.setInfo = setInfo;
	}
	
	// constructors
	
	public JavaFieldGetSet(String fieldInfo, String getInfo, String setInfo) {
		super();
		this.fieldInfo = fieldInfo;
		this.getInfo = getInfo;
		this.setInfo = setInfo;
	}
	public JavaFieldGetSet() {
		super();
	}
	
}
