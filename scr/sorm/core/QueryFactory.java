package com.sorm.core;
/**
 * ����Query����Ĺ�����
 * @author Administrator
 *
 */
public class QueryFactory {

	private static QueryFactory factory = new QueryFactory();
	private static Query prototypeObj;
	static {
		try{
			// ����ָ����query��
			Class c = Class.forName(DBManager.getConf().getQueryClass());
			prototypeObj = (Query) c.newInstance();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private QueryFactory(){ // ������˽��
	}
	public static Query createQuery(){
		try{
			return (Query)prototypeObj.clone();
		} catch (CloneNotSupportedException e){
			e.printStackTrace();
			return null;
		}
		
	/*
		try {
			return (Query)c.newInstance();  // �ή��Ч��
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	*/
	}
	
}








