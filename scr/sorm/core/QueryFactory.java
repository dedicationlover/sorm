package com.sorm.core;
/**
 * 创建Query对象的工厂类
 * @author Administrator
 *
 */
public class QueryFactory {

	private static QueryFactory factory = new QueryFactory();
	private static Query prototypeObj;
	static {
		try{
			// 加载指定的query类
			Class c = Class.forName(DBManager.getConf().getQueryClass());
			prototypeObj = (Query) c.newInstance();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private QueryFactory(){ // 构造器私有
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
			return (Query)c.newInstance();  // 会降低效率
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	*/
	}
	
}








