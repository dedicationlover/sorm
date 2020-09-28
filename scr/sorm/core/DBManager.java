package com.sorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.sorm.bean.Configuration;
import com.sorm.pool.DBConnPool;

/**
 * ����������Ϣ��ά�����Ӷ���Ĺ����������ӳع��ܣ�
 * @author Administrator
 *
 */
public class DBManager {

	/**
	 * ������Ϣ
	 */
	private static Configuration conf;
	/**
	 * ���ӳض���
	 */
	private static DBConnPool pool = null;
	
	static {
		Properties pros = new Properties();
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		conf = new Configuration();
		conf.setDriver(pros.getProperty("driver"));
		conf.setUrl(pros.getProperty("url"));
		conf.setUser(pros.getProperty("user"));
		conf.setPass(pros.getProperty("pass"));
		conf.setPoPackage(pros.getProperty("poPackage"));
		conf.setSrcPath(pros.getProperty("srcPath"));
		conf.setUsingDB(pros.getProperty("usingDB"));
		conf.setQueryClass(pros.getProperty("queryClass"));
		conf.setPoolMaxSize(Integer.parseInt(pros.getProperty("poolMaxSize")));
		conf.setPoolMinSize(Integer.parseInt(pros.getProperty("poolMinSize")));
		
	} // end static
	/**
	 * �����µ�Connection����
	 * @return Connection����
	 */
	public static Connection createConn(){
		try {
			Class.forName(conf.getDriver());
			// ֱ�ӽ������ӣ������������ӳش������Ч��
			return DriverManager.getConnection(conf.getUrl(),conf.getUser(), conf.getPass());
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	} // end createConn
	
	/**
	 * ���Connection����
	 * @return Connection����
	 */
	public static Connection getConn(){
		if(pool == null){
			pool = new DBConnPool();
		}
		return pool.getConnection();
	} // end getConn
	/**
	 * �رմ����ResultSet��PreparedStatement��Connection����
	 * @param rs ResultSet
	 * @param ps PreparedStatement
	 * @param conn Connection
	 */
	public static void close(ResultSet rs, PreparedStatement ps, Connection conn){
		try {
			if(rs != null){
				rs.close();
			}
			if(ps != null){
				ps.close();
			}
//			if(conn != null){
//				conn.close();
//			}
			pool.close(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �رմ����Statement��Connection����
	 * @param s Statement
	 * @param conn Connection
	 */
	public static void close(Statement s, Connection conn){
		try {
			if(s != null){
				s.close();
			}
//			if(conn != null){
//				conn.close();
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.close(conn);
	}
	/**
	 * �رմ����Connection����
	 * @param conn Connection���Ӷ���
	 */
	public static void close(Connection conn){
//		try {
//			if(conn != null){
//				conn.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		pool.close(conn);
	}
	/**
	 * ��ȡConfiguration����
	 * @return ����Configuration����
	 */
	public static Configuration getConf(){
		return conf;
	}
}
