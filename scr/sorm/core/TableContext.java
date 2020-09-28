package com.sorm.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sorm.bean.ColumnInfo;

import com.sorm.bean.TableInfo;
import com.sorm.utils.JavaFileUtils;
import com.sorm.utils.StringUtils;

/**
 * �����ȡ�������ݿ����б�ṹ����ṹ�Ĺ�ϵ�������Ը��ݱ�ṹ������ṹ
 * @author Administrator
 *
 */
public class TableContext {

	/**
	 * ����Ϊkey������Ϣ����Ϊvalue
	 */
	public static Map<String, TableInfo> tables = new HashMap<String, TableInfo>();
	
	/**
	 * ��po��class����ͱ���Ϣ���������������������
	 */
	public static Map<Class, TableInfo> poClassTableMap = new HashMap<Class, TableInfo>();
	
	private TableContext(){}
	
	static {
		try {
			// ��ʼ����ñ����Ϣ
			Connection conn = DBManager.getConn();
			DatabaseMetaData dbmd = conn.getMetaData();
			
			ResultSet tableRet = dbmd.getTables(null, "%", "%", new String[]{"TABLE"});
			
			while (tableRet.next()){
				String tableName = (String) tableRet.getObject("TABLE_NAME");
				
				TableInfo ti = new TableInfo(tableName, new ArrayList<ColumnInfo>(), new HashMap<String, ColumnInfo>());
				tables.put(tableName, ti);
				
				ResultSet set = dbmd.getColumns(null, "%", tableName, "%"); // ��ѯ���е������ֶ�
				while(set.next()){
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"), set.getString("TYPE_NAME"), 0);
					ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
				}
				
				ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);
				while(set2.next()){
					ColumnInfo ci2 = (ColumnInfo) ti.getColumns().get(set2.getObject("COLUMN_NAME"));
					ci2.setKeyType(1);  // ����Ϊ��������
					ti.getPriKey().add(ci2);
				}
				
				if(ti.getPriKey().size() > 0){
					ti.setOnlyPriKey(ti.getPriKey().get(0));
				}
			} // end while
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		// ������ṹ
		updateJavaPOFile();
		// ����po���µ������࣬�������ã����Ч��
		loadPOTables();
	}
	
	/**
	 * ���ݱ�ṹ���������õ�po���µ�java��
	 * ʵ���˴ӱ�ṹת������ṹ
	 */
	public static void updateJavaPOFile(){
		Map<String, TableInfo> map = TableContext.tables;
		for(TableInfo t:map.values()){
			JavaFileUtils.createJavaPOFile(t, new MySqlTypeConvertor());
		}
	}
	
	/**
	 * ����po���µ���
	 */
	public static void loadPOTables(){
		for(TableInfo tableInfo:tables.values()){
			try {
				Class c = Class.forName(DBManager.getConf().getPoPackage() + 
						"." + StringUtils.firstChar2UpperCase(tableInfo.getTname()));
				poClassTableMap.put(c, tableInfo);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	} // end loadPOTables
	
} // end Class







