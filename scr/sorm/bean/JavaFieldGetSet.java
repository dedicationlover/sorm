package com.sorm.bean;

/**
 * 封装了java属性和get、set方法的源代码
 * @author Administrator
 *
 */
public class JavaFieldGetSet {
	/**
	 * 属性的源码信息，如:private int id;
	 */
	private String fieldInfo;
	/**
	 * get方法的源码信息，如: public int get(){...}
	 */
	private String getInfo;
	/**
	 * set方法的源码信息，如: public void set(int id){...}
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
