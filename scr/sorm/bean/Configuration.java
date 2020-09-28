package com.sorm.bean;

/**
 * ����������Ϣ
 * @author Administrator
 *
 */
public class Configuration {

	/**
	 * ������
	 */
	private String driver;
	/**
	 * jdbc��url
	 */
	private String url;
	/**
	 * ���ݿ���û���
	 */
	private String user;
	/**
	 * ���ݿ�����
	 */
	private String pass;
	/**
	 * ����ʹ���ĸ����ݿ�
	 */
	private String usingDB;
	/**
	 * ��Ŀ��Դ��·��
	 */
	private String srcPath;
    /**
     * ɨ������java��İ���po��Persistence Object,�־û�����
     */
	private String poPackage;
	/**
	 * ��Ŀʹ�õĲ�ѯ������һ����
	 */
	private String queryClass;
	/**
	 * ���ӳ�����С��������
	 */
	private int poolMinSize;
	/**
	 * ���ӳ�������������
	 */
	private int poolMaxSize;
	
	public int getPoolMinSize() {
		return poolMinSize;
	}
	public void setPoolMinSize(int poolMinSize) {
		this.poolMinSize = poolMinSize;
	}
	public int getPoolMaxSize() {
		return poolMaxSize;
	}
	public void setPoolMaxSize(int poolMaxSize) {
		this.poolMaxSize = poolMaxSize;
	}
	public String getQueryClass() {
		return queryClass;
	}
	public void setQueryClass(String queryClass) {
		this.queryClass = queryClass;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getUsingDB() {
		return usingDB;
	}
	public void setUsingDB(String usingDB) {
		this.usingDB = usingDB;
	}
	public String getSrcPath() {
		return srcPath;
	}
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	public String getPoPackage() {
		return poPackage;
	}
	public void setPoPackage(String poPackage) {
		this.poPackage = poPackage;
	}
	public Configuration(String driver, String url, String user, String pass, String usingDB, String srcPath,
			String poPackage, String queryClass, int poolMinSize, int poolMaxSize) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pass = pass;
		this.usingDB = usingDB;
		this.srcPath = srcPath;
		this.poPackage = poPackage;
		this.queryClass = queryClass;
		this.poolMaxSize = poolMaxSize;
		this.poolMinSize = poolMinSize;
	}
	public Configuration() {
		super();
	}
	
	
}
