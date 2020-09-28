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
 * 根据配置信息，维持连接对象的管理（增加连接池功能）
 * @author Administrator
 *
 */
public class DBManager {

	/**
	 * 配置信息
	 */
	private static Configuration conf;
	/**
	 * 连接池对象
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
	 * 创建新的Connection对象
	 * @return Connection对象
	 */
	public static Connection createConn(){
		try {
			Class.forName(conf.getDriver());
			// 直接建立连接，后期增加连接池处理，提高效率
			return DriverManager.getConnection(conf.getUrl(),conf.getUser(), conf.getPass());
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	} // end createConn
	
	/**
	 * 获得Connection对象
	 * @return Connection对象
	 */
	public static Connection getConn(){
		if(pool == null){
			pool = new DBConnPool();
		}
		return pool.getConnection();
	} // end getConn
	/**
	 * 关闭传入的ResultSet、PreparedStatement、Connection对象
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
	 * 关闭传入的Statement、Connection对象
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
	 * 关闭传入的Connection对象
	 * @param conn Connection连接对象
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
	 * 获取Configuration对象
	 * @return 返回Configuration对象
	 */
	public static Configuration getConf(){
		return conf;
	}
}
