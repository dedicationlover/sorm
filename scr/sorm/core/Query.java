package com.sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sorm.bean.ColumnInfo;
import com.sorm.bean.TableInfo;
import com.sorm.utils.JDBCUtils;
import com.sorm.utils.ReflectUtils;

/**
 * 负责查询（对外提供服务的核心类）
 * @author Administrator
 *
 */
public abstract class Query implements Cloneable {
	
	/**
	 * 采用模板方法模式将JDBC操作封装成模板，便于重用
	 * @param sql sql语句
	 * @param params sql的参数
	 * @param clazz 记录要封装到的java类
	 * @param back CallBack的实现类，实现回调
	 * @return 查询到的结果
	 */
	public Object executeQueryTemple(String sql, Object[] params, Class clazz, CallBack back){
		Connection conn = DBManager.getConn();
		List list = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			// 给sql设参
			JDBCUtils.handleParams(ps, params);
			System.out.println(ps);
			rs = ps.executeQuery();
			
			return back.doExecute(conn, ps, rs);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBManager.close(rs, ps, conn);
		}
	}
	

	/**
	 * 直接执行一个DML语句
	 * @param sql  sql语句
	 * @param params 参数
	 * @return 执行sql语句后影响记录的行数
	 */
	public int executeDML(String sql, Object[] params){
		Connection conn = DBManager.getConn();
		int count  = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			// 给sql设参
			JDBCUtils.handleParams(ps, params);
			count = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 将一个对象存储到数据库中
	 * 把对象中不为null的属性往数据库中存储，如果数字为null则为0
	 * @param obj 要存储的对象
	 */
	public void insert(Object obj){
		// obj --> 表中  insert into 表名(id, uname, pwd) values(?,?,?)
		// 获取表名
		Class c = obj.getClass();
		List<Object> params = new ArrayList<Object>();  // 存储sql的参数对象
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		StringBuilder sql = new StringBuilder("insert into " + tableInfo.getTname() + " (");
		int countNotNullField = 0; // 计算不为空的属性个数
		Field[] fs = c.getDeclaredFields();
		for(Field f:fs){
			String fieldName = f.getName();
			Object fieldValue = ReflectUtils.invokeGet(fieldName, obj);
			if(fieldValue != null){
				sql.append(fieldName + ",");
				countNotNullField++;
				params.add(fieldValue);
			}
		}
		sql.setCharAt(sql.length()-1, ')');
		sql.append(" values (");
		// ?
		while(countNotNullField-- > 0){
			sql.append("?,");
		}
		sql.setCharAt(sql.length()-1, ')');
		executeDML(sql.toString(), params.toArray());
	}
	
	/**
	 * 删除clazz表示类对应表中的记录（指定主键值id的记录）
	 * @param clazz  与表对应的类的Class对象
	 * @param id  主键的值
	 */
	public void delete(Class clazz, Object id){
		// student_table, 4 --> delete from student_table where id=4
		// 通过Class对象找TableInfo
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		// 获得主键
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		
		String sql = "delete from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + " = ?";
		
		executeDML(sql, new Object[]{id});
	}
	
	/**
	 * 删除对象在数据库中对应的记录（对象所在的类对象到表，对象的主键值对应到记录）
	 * @param obj 要删除的对象（记录）
	 */
	public void delete(Object obj){
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		// 获得主键
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		
		// 通过反射机制，调用属性对应的set方法和get方法
		Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getName(), obj);
		delete(c, priKeyValue);
	}
	
	/**
	 * 更新对象对应的记录，并且只更新指定的字段的值
	 * @param obj 所更新的对象
	 * @param fieldNames 更新的属性列表
	 * @return 执行sql语句后影响记录的行数
	 */
	public int update(Object obj, String[] fieldNames){
		// obj{"uname", "pwd"}---> update 表名 set uname=?,pwd=? where id=?
		Class c = obj.getClass();
		List<Object> params = new ArrayList<Object>();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		ColumnInfo priKey = tableInfo.getOnlyPriKey();
		StringBuilder sql = new StringBuilder("update " + tableInfo.getTname() + " set ");
		
		for(String fname:fieldNames){
			Object fvalue = ReflectUtils.invokeGet(fname, obj);
			sql.append(fname + "=?,");
			params.add(fvalue);
		}
		sql.setCharAt(sql.length()-1, ' ');
		sql.append(" where ");
		sql.append(priKey.getName() + "=? ");
		params.add(ReflectUtils.invokeGet(priKey.getName(), obj));
		System.out.println(sql);
		return executeDML(sql.toString(), params.toArray());
	}
	
	/**
	 * 查询返回多行记录，并将每行记录封装到clazz指定类的对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据的Javabean类的Class对象
	 * @param params sql的参数
	 * @return 查询到的结果
	 */
	public List queryRows(final String sql, final Class clazz, final Object[] params){
		
		return (List)executeQueryTemple(sql, params, clazz, new CallBack(){

			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				List list = null;
				try {
					ResultSetMetaData metaData = rs.getMetaData();
					// 多行
					while(rs.next()){
						if(null == list){
							list = new ArrayList();
						}
						Object rowObject = clazz.newInstance();
						// 多列 select username, pwd, age from user where id>? and age>18
						for(int i = 0; i < metaData.getColumnCount(); i++){
							String columnName = metaData.getColumnLabel(i+1);
							Object columnValue = rs.getObject(i+1);
							// 调用rowObj对象的setUsername方法，将columnValue的值设置进去
							ReflectUtils.invokeSet(rowObject, columnName, columnValue);
						}
						list.add(rowObject);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return list;
			}
			
		});
	
	}
	
	/**
	 * 查询返回一行记录，并将每行记录封装到clazz指定类的对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据的Javabean类的Class对象
	 * @param params sql的参数
	 * @return 查询到的结果
	 */
	public Object queryUniqueRow(String sql, Class clazz, Object[] params){
		List list = queryRows(sql, clazz, params);
		return (list == null || list.size() <= 0) ? null : list.get(0);
	}
	
	/**
	 * 查询返回一个值（一行一列），并将该值返回
	 * @param sql 查询语句
	 * @param params sql的参数
	 * @return 查询到的结果
	 */
	public Object queryValue(String sql, Object[] params){
		return executeQueryTemple(sql, params, null, new CallBack(){

			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				Object value = null;
				try {
					while(rs.next()){
						value = rs.getObject(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return value;
			}
			
		});
	}
	
	/**
	 * 查询返回一个数字（一行一列），并将该值返回
	 * @param sql 查询语句
	 * @param params sql的参数
	 * @return 查询到的数字
	 */
	public Number queryNumber(String sql, Object[] params){
		return (Number)queryValue(sql, params);
	}

	/**
	 * 分页查询
	 * @param pageNum 第几页数据
	 * @param size 每页显示多少记录
	 * @return 查询对象
	 */
	public abstract Object queryPagenate(int pageNum, int size);
	
	@Override
	protected Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
}















