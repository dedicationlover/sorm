package com.sorm.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sorm.core.DBManager;

/**
 * 连接池的类
 * @author Administrator
 *
 */
public class DBConnPool {

	/**
	 * 连接池对象
	 */
	private List<Connection> pool;
	/**
	 * 最大连接数
	 */
	private static final int POOL_MAX_SIZE = DBManager.getConf().getPoolMaxSize();
	/**
	 * 最小连接数
	 */
	private static final int POOL_MIN_SIZE = DBManager.getConf().getPoolMinSize();
	
	
	/**
	 * 初始化连接池，使池中的连接数达到最小值
	 */
	public void initPool(){
		if(pool == null){
			pool = new ArrayList<Connection>();
		}
		while(pool.size() < DBConnPool.POOL_MIN_SIZE){
			pool.add(DBManager.createConn());
			System.out.println("初始化连接池中连接个数：" + pool.size());
		}
	}
	/**
	 * 从连接池中取出一个连接
	 * @return 返回一个连接
	 */
	public synchronized Connection getConnection(){
		int last_index = pool.size() - 1;
		Connection conn = pool.get(last_index);
		// 将该连接从池中删除（防止被多人同时用）
		pool.remove(last_index);
		return conn;
	}
	/**
	 * 将连接放回池中
	 * @param conn 放回池中的连接
	 */
	public synchronized void close(Connection conn){
		if(pool.size() >= POOL_MAX_SIZE){
			try {
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			pool.add(conn);
		} 
	}

	public DBConnPool(){
		initPool();
	}
}
















